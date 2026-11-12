package com.aistudio.qandparsi.ads

import ir.tapsell.mediation.ad.request.BannerSize

/**
 * Tapsell Mediation identifiers.
 *
 * The application key is supplied through the `TapsellMediationAppKey` manifest placeholder in
 * `app/build.gradle.kts`, because the SDK reads it from the merged manifest.
 *
 * ⚠️ The zone ids below are the public test zones published with the official Tapsell sample.
 * Replace them (and the app key in `build.gradle.kts`) with the zones created for
 * «قند پارسی» in the Tapsell dashboard before releasing on Cafe Bazaar.
 */
object AdConfig {
    const val BANNER_ZONE_ID: String = "e3d5999c-5990-4e31-8ce9-642ce040a7f4"
    const val REWARDED_ZONE_ID: String = "1d710cc7-5e96-46ac-a3e9-8463300333e6"

    val BANNER_SIZE: BannerSize = BannerSize.BANNER_320_50
}
