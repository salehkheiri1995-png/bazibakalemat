package com.example.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_progress")
data class PlayerProgressEntity(
    @PrimaryKey
    val levelId: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val starsEarned: Int = 0, // 0 to 3 stars
    val foundWordsRaw: String = "", // Comma-separated list of found words
    val highScore: Int = 0,
    val completionTimeSeconds: Int = 0,
    val lastPlayedTimestamp: Long = System.currentTimeMillis()
) {
    fun getFoundWordsSet(): Set<String> =
        if (foundWordsRaw.isBlank()) emptySet()
        else foundWordsRaw.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
}
