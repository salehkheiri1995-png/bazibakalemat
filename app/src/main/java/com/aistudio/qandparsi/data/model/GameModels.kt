package com.aistudio.qandparsi.data.model

data class GameLevel(
    val id: Int,
    val city: String,
    val cityNameFa: String,
    val stageNumber: Int,
    val letters: List<Char>,
    val targetWords: List<String>,
    val bonusWords: List<String> = emptyList(),
    val rewardCoins: Int = 50,
    val rewardGems: Int = 1
)

data class ProverbLevel(
    val id: Int,
    val proverbFull: String,
    val maskedProverb: String,
    val missingWord: String,
    val meaning: String,
    val emojiClue: String,
    val scrambledLetters: List<Char>,
    val rewardCoins: Int = 60
)

data class DailyChallenge(
    val dateString: String,
    val title: String,
    val letters: List<Char>,
    val targetWords: List<String>,
    val rewardCoins: Int = 150,
    val rewardGems: Int = 3
)

data class ShopProduct(
    val id: String,
    val sku: String,
    val titleFa: String,
    val descriptionFa: String,
    val priceToman: Long,
    val coins: Int,
    val gems: Int,
    val isBestValue: Boolean = false,
    val isVip: Boolean = false,
    val badgeText: String? = null
)

data class Achievement(
    val id: String,
    val titleFa: String,
    val descriptionFa: String,
    val rewardCoins: Int,
    val currentProgress: Int,
    val maxProgress: Int,
    val isClaimed: Boolean
)

data class LeaderboardUser(
    val rank: Int,
    val nameFa: String,
    val provinceFa: String,
    val score: Int,
    val avatarColorHex: Long,
    val isCurrentPlayer: Boolean = false
)

data class WheelReward(
    val id: Int,
    val labelFa: String,
    val coins: Int,
    val gems: Int = 0,
    val freeLetterHints: Int = 0,
    val freeWordHints: Int = 0,
    val colorHex: Long,
    val iconEmoji: String = "🪙",
    val isJackpot: Boolean = false
)
data class DailyLoginReward(
    val dayNumber: Int,
    val dayTitleFa: String,
    val coins: Int,
    val gems: Int = 0,
    val freeLetterHints: Int = 0,
    val freeWordHints: Int = 0,
    val isGrandPrize: Boolean = false,
    val iconEmoji: String = "🎁"
)

enum class ScreenRoute {
    HOME,
    WORD_GAME,
    PROVERB_GAME,
    DAILY_CHALLENGE,
    SPEED_LEAGUE,
    FORTUNE_WHEEL,
    BAZAAR_STORE,
    CITY_MAP
}

enum class SoundType {
    CLICK,
    LETTER_PICK,
    WORD_FOUND,
    LEVEL_WON,
    ERROR,
    COIN_EARNED,
    WHEEL_SPIN
}
