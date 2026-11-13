package com.aistudio.qandparsi.ads

import com.aistudio.qandparsi.BuildConfig
import ir.tapsell.mediation.ad.request.BannerSize

/**
 * Tapsell Mediation identifiers.
 *
 * The application key is supplied through the `TapsellMediationAppKey` manifest placeholder in
 * `app/build.gradle.kts`, because the SDK reads it from the merged manifest.
 *
 * The banner and rewarded zone ids below come from BuildConfig, which is populated from the
 * `TAPSELL_BANNER_ZONE_ID` / `TAPSELL_REWARDED_ZONE_ID` environment variables (or Gradle
 * properties). Until you provide real zones, the official public sample test zones are used:
 *
 *   - App key:    `TAPSELL_APP_KEY` (defaults to the official sample key)
 *   - Banner:     `TAPSELL_BANNER_ZONE_ID`
 *   - Rewarded:   `TAPSELL_REWARDED_ZONE_ID`
 *
 * Set all three to your production values (from the Tapsell dashboard for this app) before
 * releasing on Cafe Bazaar.
 */
object AdConfig {
    const val BANNER_ZONE_ID: String = BuildConfig.TAPSELL_BANNER_ZONE_ID
    const val REWARDED_ZONE_ID: String = BuildConfig.TAPSELL_REWARDED_ZONE_ID

    val BANNER_SIZE: BannerSize = BannerSize.BANNER_320_50
}
