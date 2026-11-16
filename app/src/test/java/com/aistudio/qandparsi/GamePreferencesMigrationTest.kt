package com.aistudio.qandparsi

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.aistudio.qandparsi.data.local.GamePreferences
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** Guards the 35-level → 3000-level save migration. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GamePreferencesMigrationTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val prefs = context.getSharedPreferences("qand_parsi_prefs", Context.MODE_PRIVATE)

    @After
    fun tearDown() {
        prefs.edit().clear().commit()
    }

    @Test
    fun legacyGilanStageOneMovesToTheNewGilanStart() {
        prefs.edit()
            .putInt("current_level_idx", 21) // legacy Gilan stage 1 (5th city)
            .putInt("data_version", 1)
            .commit()

        val gamePreferences = GamePreferences(context)

        // City index 4 × 20 stages + stage 1 = 81.
        assertEquals(81, gamePreferences.currentLevel)
        assertEquals(2, gamePreferences.dataVersion)
    }

    @Test
    fun aFullyClearedLegacyMapContinuesWithTheNextNewCity() {
        prefs.edit()
            .putInt("current_level_idx", 36) // all 35 legacy levels cleared
            .putInt("data_version", 1)
            .commit()

        val gamePreferences = GamePreferences(context)

        assertEquals(141, gamePreferences.currentLevel)
    }

    @Test
    fun aFreshInstallStaysOnLevelOne() {
        prefs.edit().putInt("data_version", 1).commit()

        val gamePreferences = GamePreferences(context)

        assertEquals(1, gamePreferences.currentLevel)
        assertEquals(2, gamePreferences.dataVersion)
    }
}
