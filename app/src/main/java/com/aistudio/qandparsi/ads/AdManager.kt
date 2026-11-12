package com.aistudio.qandparsi.ads

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.show.AdShowCompletionState
import ir.tapsell.mediation.ad.views.banner.BannerContainer
import java.lang.ref.WeakReference

/**
 * Thin wrapper around Tapsell Mediation.
 *
 * Rewarded videos are preloaded and cached. A reward is reported to the caller only from the
 * SDK's `onRewarded()` callback, so a failed, skipped or closed ad never grants in-game currency.
 *
 * All shared mutable state is guarded by [lock] because Tapsell SDK callbacks can arrive on
 * background threads before being forwarded to the main looper.
 */
object AdManager {

    private const val TAG = "AdManager"
    private const val LOAD_TIMEOUT_MS = 15_000L

    interface RewardedAdListener {
        /** The request is in flight; the UI may show a loading state. */
        fun onLoading()
        /** The video is actually on screen. */
        fun onShown()
        /** The SDK confirmed a completed, reward-eligible view. */
        fun onRewarded()
        /** The video was closed (after or without a reward). */
        fun onClosed()
        /** The ad could not be loaded or shown. */
        fun onFailed(message: String)
    }

    private val lock = Any()
    private val mainHandler = Handler(Looper.getMainLooper())

    private var initialized = false

    private var rewardedAdId: String? = null
    private var isLoadingRewarded = false

    private var pendingListener: RewardedAdListener? = null
    private var pendingActivity: WeakReference<Activity>? = null

    private val loadTimeout = Runnable {
        val listener: RewardedAdListener?
        synchronized(lock) {
            listener = pendingListener
            pendingListener = null
            pendingActivity = null
            rewardedAdId = null
            isLoadingRewarded = false
        }
        listener?.onFailed("Rewarded ad load timed out")
    }

    fun initialize(context: Context) {
        synchronized(lock) {
            if (initialized) return
            initialized = true
        }
        runCatching {
            Tapsell.initialize(context.applicationContext)
        }.onFailure { Log.e(TAG, "Tapsell initialization failed", it) }
        preloadRewarded()
    }

    /** Starts loading a rewarded video if none is cached. Safe to call repeatedly. */
    fun preloadRewarded() {
        synchronized(lock) {
            if (rewardedAdId != null || isLoadingRewarded) return
            isLoadingRewarded = true
        }
        runCatching {
            Tapsell.requestRewardedAd(
                AdConfig.REWARDED_ZONE_ID,
                object : RequestResultListener {
                    override fun onSuccess(adId: String) {
                        mainHandler.post { onRewardedLoaded(adId) }
                    }

                    override fun onFailure(message: String) {
                        mainHandler.post {
                            val listener = takePending()
                            synchronized(lock) { isLoadingRewarded = false }
                            listener?.onFailed(message)
                        }
                    }
                }
            )
        }.onFailure {
            val listener = takePending()
            synchronized(lock) { isLoadingRewarded = false }
            listener?.onFailed(it.message ?: "Rewarded ad request failed")
        }
    }

    private fun onRewardedLoaded(adId: String) {
        var listener: RewardedAdListener? = null
        var activity: Activity? = null
        synchronized(lock) {
            isLoadingRewarded = false
            rewardedAdId = null
            mainHandler.removeCallbacks(loadTimeout)
            listener = pendingListener
            activity = pendingActivity?.get()
            pendingListener = null
            pendingActivity = null
        }

        if (listener == null) {
            // Nobody is waiting; cache the loaded ad for later.
            synchronized(lock) { rewardedAdId = adId }
            return
        }

        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            listener.onFailed("Activity is no longer available")
            return
        }
        showInternal(activity, adId, listener)
    }

    /**
     * Shows a rewarded video. If no video is cached yet, the request is queued and the video
     * opens as soon as it becomes available (or [RewardedAdListener.onFailed] fires on timeout).
     */
    fun showRewarded(activity: Activity, listener: RewardedAdListener) {
        val cachedAdId: String?
        synchronized(lock) {
            cachedAdId = rewardedAdId
            if (cachedAdId != null) {
                rewardedAdId = null
                mainHandler.removeCallbacks(loadTimeout)
                // Only queue if we don't already have a pending request.
                if (pendingListener == null) {
                    pendingListener = listener
                    pendingActivity = WeakReference(activity)
                }
            } else {
                pendingListener = listener
                pendingActivity = WeakReference(activity)
                mainHandler.removeCallbacks(loadTimeout)
                mainHandler.postDelayed(loadTimeout, LOAD_TIMEOUT_MS)
            }
        }

        listener.onLoading()

        val adToShow = cachedAdId
        if (adToShow == null) {
            preloadRewarded()
        } else {
            showInternal(activity, adToShow, listener)
        }
    }

    private fun showInternal(activity: Activity, adId: String, listener: RewardedAdListener) {
        var rewardDelivered = false
        val stateListener = object : AdStateListener.Rewarded {
            override fun onAdClicked() = Unit

            override fun onAdClosed(completionState: AdShowCompletionState) {
                listener.onClosed()
                preloadRewarded()
            }

            override fun onAdFailed(message: String) {
                listener.onFailed(message)
                preloadRewarded()
            }

            override fun onAdImpression() {
                listener.onShown()
            }

            override fun onRewarded() {
                // Guard against a duplicated callback from a mediation adapter.
                if (rewardDelivered) return
                rewardDelivered = true
                listener.onRewarded()
            }
        }

        runCatching {
            Tapsell.showRewardedAd(adId, activity, stateListener)
        }.onFailure {
            listener.onFailed(it.message ?: "Rewarded ad show failed")
            preloadRewarded()
        }
    }

    /** Requests a banner. [onLoaded] receives the ad id used by [showBanner]/[destroyBanner]. */
    fun loadBanner(
        zoneId: String,
        onLoaded: (String) -> Unit,
        onFailed: (String) -> Unit
    ) {
        runCatching {
            Tapsell.requestBannerAd(
                zoneId,
                AdConfig.BANNER_SIZE,
                object : RequestResultListener {
                    override fun onSuccess(adId: String) {
                        mainHandler.post { onLoaded(adId) }
                    }

                    override fun onFailure(message: String) {
                        mainHandler.post { onFailed(message) }
                    }
                }
            )
        }.onFailure { onFailed(it.message ?: "Banner request failed") }
    }

    fun showBanner(adId: String, container: BannerContainer, activity: Activity) {
        runCatching {
            Tapsell.showBannerAd(adId, container, activity)
        }.onFailure { Log.e(TAG, "Banner show failed", it) }
    }

    fun destroyBanner(adId: String) {
        runCatching { Tapsell.destroyBannerAd(adId) }
    }

    /** Atomically claims and clears the pending listener/reward pair. */
    private fun takePending(): RewardedAdListener? = synchronized(lock) {
        val listener = pendingListener
        pendingListener = null
        pendingActivity = null
        mainHandler.removeCallbacks(loadTimeout)
        listener
    }
}
