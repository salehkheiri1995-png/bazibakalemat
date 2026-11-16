package com.aistudio.qandparsi.data.local

import android.content.Context
import android.content.SharedPreferences

class GamePreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("qand_parsi_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_COINS = "user_coins"
        private const val KEY_GEMS = "user_gems"
        private const val KEY_CURRENT_LEVEL = "current_level_idx"
        private const val KEY_CURRENT_PROVERB_LEVEL = "current_proverb_idx"
        private const val KEY_FOUND_WORDS_PREFIX = "found_words_lvl_"
        private const val KEY_FOUND_BONUS_WORDS_PREFIX = "found_bonus_words_lvl_"
        private const val KEY_DAILY_FOUND_WORDS_PREFIX = "daily_found_words_"
        private const val KEY_BONUS_WORDS_COUNT = "bonus_words_count"
        private const val KEY_PIGGY_BANK_FULL_CLAIMED = "piggy_bank_claimed_count"
        private const val KEY_LAST_WHEEL_SPIN_TIME = "last_wheel_spin_time"
        private const val KEY_DAILY_STREAK = "daily_streak_count"
        private const val KEY_LAST_DAILY_DATE = "last_daily_date"
        private const val KEY_LAST_DAILY_REWARD_DATE = "last_daily_challenge_reward_date"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val KEY_ADS_REMOVED = "ads_removed"
        private const val KEY_IS_VIP = "is_vip_subscriber"
        private const val KEY_STARS_PREFIX = "stars_lvl_"
        private const val KEY_SPEED_HIGHSCORE = "speed_highscore"
        private const val KEY_LAST_LOGIN_BONUS_TIME = "last_login_bonus_claim_time"
        private const val KEY_LOGIN_BONUS_DAY = "login_bonus_streak_day"
        private const val KEY_FREE_LETTER_HINTS = "powerup_free_letter_hints"
        private const val KEY_FREE_WORD_HINTS = "powerup_free_word_hints"
        private const val KEY_DAILY_TUTORIAL_SEEN = "daily_challenge_tutorial_seen"
        private const val KEY_OPENED_CITY_CHESTS = "opened_city_chests"
        private const val KEY_SEEN_STORY_PAGES = "seen_story_pages"
        private const val KEY_DATA_VERSION = "data_version"

        /**
         * Version 2 is the 150-city map (20 stages per city). Version 1 was the legacy
         * 35-level map (5 stages for each of the first seven cities).
         */
        private const val DATA_VERSION = 2
        private const val LEGACY_STAGES_PER_CITY = 5
        private const val CURRENT_STAGES_PER_CITY = 20
    }

    var coins: Int
        get() = prefs.getInt(KEY_COINS, 300) // 300 starting coins welcome gift!
        set(value) = prefs.edit().putInt(KEY_COINS, value.coerceAtLeast(0)).apply()

    var gems: Int
        get() = prefs.getInt(KEY_GEMS, 12) // 12 starting gems
        set(value) = prefs.edit().putInt(KEY_GEMS, value.coerceAtLeast(0)).apply()

    var currentLevel: Int
        get() = prefs.getInt(KEY_CURRENT_LEVEL, 1)
        set(value) = prefs.edit().putInt(KEY_CURRENT_LEVEL, value).apply()

    var currentProverbLevel: Int
        get() = prefs.getInt(KEY_CURRENT_PROVERB_LEVEL, 1)
        set(value) = prefs.edit().putInt(KEY_CURRENT_PROVERB_LEVEL, value).apply()

    var bonusWordsCount: Int
        get() = prefs.getInt(KEY_BONUS_WORDS_COUNT, 0)
        set(value) = prefs.edit().putInt(KEY_BONUS_WORDS_COUNT, value).apply()

    var lastWheelSpinTime: Long
        get() = prefs.getLong(KEY_LAST_WHEEL_SPIN_TIME, 0L)
        set(value) = prefs.edit().putLong(KEY_LAST_WHEEL_SPIN_TIME, value).apply()

    var dailyStreak: Int
        get() = prefs.getInt(KEY_DAILY_STREAK, 1)
        set(value) = prefs.edit().putInt(KEY_DAILY_STREAK, value).apply()

    var lastDailyDate: String
        get() = prefs.getString(KEY_LAST_DAILY_DATE, "") ?: ""
        set(value) = prefs.edit().putString(KEY_LAST_DAILY_DATE, value).apply()

    var lastDailyChallengeRewardDate: String
        get() = prefs.getString(KEY_LAST_DAILY_REWARD_DATE, "") ?: ""
        set(value) = prefs.edit().putString(KEY_LAST_DAILY_REWARD_DATE, value).apply()

    var isSoundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND_ENABLED, value).apply()

    var isVibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, value).apply()

    var isAdsRemoved: Boolean
        get() = prefs.getBoolean(KEY_ADS_REMOVED, false)
        set(value) = prefs.edit().putBoolean(KEY_ADS_REMOVED, value).apply()

    var isVip: Boolean
        get() = prefs.getBoolean(KEY_IS_VIP, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_VIP, value).apply()

    var speedHighScore: Int
        get() = prefs.getInt(KEY_SPEED_HIGHSCORE, 0)
        set(value) = prefs.edit().putInt(KEY_SPEED_HIGHSCORE, value).apply()

    var lastLoginBonusTime: Long
        get() = prefs.getLong(KEY_LAST_LOGIN_BONUS_TIME, 0L)
        set(value) = prefs.edit().putLong(KEY_LAST_LOGIN_BONUS_TIME, value).apply()

    var loginBonusDay: Int
        get() = prefs.getInt(KEY_LOGIN_BONUS_DAY, 1)
        set(value) = prefs.edit().putInt(KEY_LOGIN_BONUS_DAY, value.coerceIn(1, 7)).apply()

    var freeLetterHints: Int
        get() = prefs.getInt(KEY_FREE_LETTER_HINTS, 0)
        set(value) = prefs.edit().putInt(KEY_FREE_LETTER_HINTS, value.coerceAtLeast(0)).apply()

    var freeWordHints: Int
        get() = prefs.getInt(KEY_FREE_WORD_HINTS, 0)
        set(value) = prefs.edit().putInt(KEY_FREE_WORD_HINTS, value.coerceAtLeast(0)).apply()

    var hasSeenDailyTutorial: Boolean
        get() = prefs.getBoolean(KEY_DAILY_TUTORIAL_SEEN, false)
        set(value) = prefs.edit().putBoolean(KEY_DAILY_TUTORIAL_SEEN, value).apply()

    /** Keys of the city chests the player already opened (one reward per city). */
    var openedCityChests: Set<String>
        get() = prefs.getStringSet(KEY_OPENED_CITY_CHESTS, emptySet())?.toSet() ?: emptySet()
        set(value) = prefs.edit().putStringSet(KEY_OPENED_CITY_CHESTS, value.toSet()).apply()

    /** Keys of the diary pages the player has already read. */
    var seenStoryPages: Set<String>
        get() = prefs.getStringSet(KEY_SEEN_STORY_PAGES, emptySet())?.toSet() ?: emptySet()
        set(value) = prefs.edit().putStringSet(KEY_SEEN_STORY_PAGES, value.toSet()).apply()

    fun markStoryPageSeen(pageKey: String) {
        val current = seenStoryPages.toMutableSet()
        if (current.add(pageKey)) {
            seenStoryPages = current
        }
    }

    var dataVersion: Int
        get() = prefs.getInt(KEY_DATA_VERSION, 1)
        private set(value) = prefs.edit().putInt(KEY_DATA_VERSION, value).apply()

    init {
        if (dataVersion < DATA_VERSION) {
            migrateLegacyMapProgress()
            dataVersion = DATA_VERSION
        }
    }

    /**
     * Keeps old players in the city they had reached: the legacy map had 5 stages per city,
     * so level `n` becomes `city * 20 + stageWithinCity` on the new 20-stage map.
     */
    private fun migrateLegacyMapProgress() {
        val legacyLevel = currentLevel
        if (legacyLevel == 7 * LEGACY_STAGES_PER_CITY + 1) {
            // The legacy map was fully cleared: continue with the first new city.
            currentLevel = 7 * CURRENT_STAGES_PER_CITY + 1
            return
        }
        if (legacyLevel <= 1 || legacyLevel > 7 * LEGACY_STAGES_PER_CITY) return
        val cityIndex = (legacyLevel - 1) / LEGACY_STAGES_PER_CITY
        val stageWithinCity = (legacyLevel - 1) % LEGACY_STAGES_PER_CITY + 1
        currentLevel = cityIndex * CURRENT_STAGES_PER_CITY + stageWithinCity
    }

    fun getFoundWords(levelId: Int): Set<String> {
        return prefs.getStringSet("$KEY_FOUND_WORDS_PREFIX$levelId", emptySet())?.toSet() ?: emptySet()
    }

    fun saveFoundWord(levelId: Int, word: String) {
        val current = getFoundWords(levelId).toMutableSet()
        current.add(word)
        prefs.edit().putStringSet("$KEY_FOUND_WORDS_PREFIX$levelId", current).apply()
    }

    /**
     * Keeps only words that still exist in the current target list. This protects players
     * upgrading from an older build whose level data was changed.
     */
    fun replaceFoundWords(levelId: Int, words: Set<String>) {
        prefs.edit().putStringSet("$KEY_FOUND_WORDS_PREFIX$levelId", words.toSet()).apply()
    }

    fun getFoundBonusWords(levelId: Int): Set<String> {
        return prefs.getStringSet("$KEY_FOUND_BONUS_WORDS_PREFIX$levelId", emptySet())?.toSet()
            ?: emptySet()
    }

    fun saveFoundBonusWord(levelId: Int, word: String) {
        val current = getFoundBonusWords(levelId).toMutableSet()
        current.add(word)
        prefs.edit().putStringSet("$KEY_FOUND_BONUS_WORDS_PREFIX$levelId", current).apply()
    }

    fun replaceFoundBonusWords(levelId: Int, words: Set<String>) {
        prefs.edit().putStringSet("$KEY_FOUND_BONUS_WORDS_PREFIX$levelId", words.toSet()).apply()
    }

    fun getDailyFoundWords(dateKey: String): Set<String> {
        return prefs.getStringSet("$KEY_DAILY_FOUND_WORDS_PREFIX$dateKey", emptySet())?.toSet()
            ?: emptySet()
    }

    fun saveDailyFoundWord(dateKey: String, word: String) {
        val current = getDailyFoundWords(dateKey).toMutableSet()
        current.add(word)
        prefs.edit().putStringSet("$KEY_DAILY_FOUND_WORDS_PREFIX$dateKey", current).apply()
    }

    fun replaceDailyFoundWords(dateKey: String, words: Set<String>) {
        prefs.edit().putStringSet("$KEY_DAILY_FOUND_WORDS_PREFIX$dateKey", words.toSet()).apply()
    }

    fun getLevelStars(levelId: Int): Int {
        return prefs.getInt("$KEY_STARS_PREFIX$levelId", 0)
    }

    fun saveLevelStars(levelId: Int, stars: Int) {
        val current = getLevelStars(levelId)
        if (stars > current) {
            prefs.edit().putInt("$KEY_STARS_PREFIX$levelId", stars).apply()
        }
    }
}
