package com.aistudio.qandparsi.ads

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import ir.tapsell.mediation.ad.views.banner.BannerContainer

/**
 * Compose wrapper for a Tapsell standard banner.
 *
 * The banner is destroyed when it leaves the composition so Tapsell can recycle the ad view.
 */
@Composable
fun TapsellBannerAd(
    modifier: Modifier = Modifier,
    zoneId: String = AdConfig.BANNER_ZONE_ID
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val container = remember(context) { BannerContainer(context) }
    var adId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(zoneId, activity) {
        AdManager.loadBanner(
            zoneId = zoneId,
            onLoaded = { loadedAdId ->
                val host = activity
                if (host != null) {
                    adId = loadedAdId
                    AdManager.showBanner(loadedAdId, container, host)
                }
            },
            onFailed = { /* Banner is optional; keep the layout usable without it. */ }
        )
    }

    DisposableEffect(zoneId, container) {
        onDispose {
            adId?.let { AdManager.destroyBanner(it) }
            adId = null
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { container }
    )
}
