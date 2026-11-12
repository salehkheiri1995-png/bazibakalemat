package com.aistudio.qandparsi.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_levels")
data class LevelEntity(
    @PrimaryKey
    val levelId: Int,
    val cityId: String,
    val cityNameFa: String,
    val stageNumber: Int,
    val lettersRaw: String, // Comma separated letters (e.g. "س,ل,ا,م")
    val targetWordsRaw: String, // Comma separated words (e.g. "سلام,مال,لمس")
    val bonusWordsRaw: String = "",
    val rewardCoins: Int = 50,
    val rewardGems: Int = 1
) {
    fun getLettersList(): List<Char> =
        lettersRaw.split(",").mapNotNull { it.trim().firstOrNull() }

    fun getTargetWordsList(): List<String> =
        targetWordsRaw.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    fun getBonusWordsList(): List<String> =
        if (bonusWordsRaw.isBlank()) emptyList()
        else bonusWordsRaw.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
