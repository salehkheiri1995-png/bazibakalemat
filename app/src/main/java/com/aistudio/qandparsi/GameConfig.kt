package com.aistudio.qandparsi

/**
 * Centralised game economy constants. Keeps magic numbers out of the ViewModel.
 */
object GameConfig {
    // Hint costs (coins)
    const val HINT_LETTER_COIN_COST = 50
    const val HINT_WORD_COIN_COST = 120

    // Rewards
    const val COINS_PER_TARGET_WORD = 25
    const val COINS_PER_BONUS_WORD = 10
    const val COINS_PER_DAILY_WORD = 10
    const val COINS_PER_AD_REWARD = 60
    const val GEMS_PER_AD_REWARD = 1

    // Fortune wheel
    const val WHEEL_SPIN_COST = 50
    const val WHEEL_FREE_SPIN_HOURS = 24

    // Piggy bank
    const val PIGGY_BANK_MIN_WORDS = 20
    const val PIGGY_BANK_COINS_PER_WORD = 5

    // Starting wallet (used by GamePreferences)
    const val STARTING_COINS = 300
    const val STARTING_GEMS = 12
}
