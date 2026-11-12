package com.aistudio.qandparsi.billing

import android.util.Log
import androidx.activity.ComponentActivity
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import ir.cafebazaar.poolakey.entity.PurchaseInfo
import ir.cafebazaar.poolakey.request.PurchaseRequest

/**
 * Thin wrapper around the Cafe Bazaar Poolakey billing SDK.
 *
 * Purchases are only permitted when [rsaPublicKey] is set (the developer's public key from the
 * Cafe Bazaar console). Poolakey verifies each purchase signature locally against that key, so a
 * [PurchaseResultListener.onPurchaseSucceed] callback implies a genuine, verified purchase. While
 * the key is empty, [purchase] fails immediately and nothing is granted.
 */
class BazaarBillingManager(
    private val activity: ComponentActivity,
    private val rsaPublicKey: String
) {

    interface PurchaseResultListener {
        fun onPurchaseSucceed(info: PurchaseInfo)
        fun onPurchaseCanceled()
        fun onPurchaseFailed(message: String)
    }

    val isConfigured: Boolean
        get() = rsaPublicKey.isNotBlank()

    private val payment: Payment? by lazy {
        if (!isConfigured) {
            Log.w(TAG, "Bazaar RSA public key not configured; purchases are disabled")
            null
        } else {
            Payment(
                context = activity,
                config = PaymentConfiguration(
                    localSecurityCheck = SecurityCheck.Enable(rsaPublicKey = rsaPublicKey),
                    shouldSupportSubscription = true
                )
            )
        }
    }

    private var connection: Connection? = null

    fun connect() {
        val p = payment ?: return
        runCatching {
            connection = p.connect {
                connectionSucceed { Log.i(TAG, "Connected to Bazaar billing service") }
                connectionFailed { Log.e(TAG, "Bazaar connection failed", it) }
                disconnected { }
            }
        }.onFailure { Log.e(TAG, "connect() threw", it) }
    }

    fun disconnect() {
        runCatching { connection?.disconnect() }
        connection = null
    }

    /**
     * Starts a purchase for [sku]. [isSubscription] routes to the subscription flow (used for the
     * monthly VIP pass). Results are delivered through [listener].
     */
    fun purchase(sku: String, isSubscription: Boolean, listener: PurchaseResultListener) {
        val p = payment
        if (p == null) {
            listener.onPurchaseFailed("پرداخت کافه بازار هنوز پیکربندی نشده است")
            return
        }
        val request = PurchaseRequest(
            productId = sku,
            payload = "qand_parsi_payload",
            dynamicPriceToken = null
        )
        val block: ir.cafebazaar.poolakey.callback.PurchaseCallback.() -> Unit = {
            purchaseFlowBegan { }
            failedToBeginFlow { listener.onPurchaseFailed(it.message ?: "شروع پرداخت ممکن نشد") }
            purchaseSucceed { info ->
                listener.onPurchaseSucceed(info)
                // Consumables must be consumed so they can be bought again.
                if (!isSubscription) {
                    p.consumeProduct(info.purchaseToken) {
                        consumeSucceed { Log.i(TAG, "Consumed ${info.productId}") }
                        consumeFailed { Log.w(TAG, "Consume failed for ${info.productId}") }
                    }
                }
            }
            purchaseCanceled { listener.onPurchaseCanceled() }
            purchaseFailed { listener.onPurchaseFailed(it.message ?: "خرید ناموفق بود") }
        }

        runCatching {
            if (isSubscription) {
                p.subscribeProduct(activity.activityResultRegistry, request, block)
            } else {
                p.purchaseProduct(activity.activityResultRegistry, request, block)
            }
        }.onFailure { listener.onPurchaseFailed(it.message ?: "شروع پرداخت ممکن نشد") }
    }

    private companion object {
        const val TAG = "BazaarBilling"
    }
}
