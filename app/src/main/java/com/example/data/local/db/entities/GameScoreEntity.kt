package com.example.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_scores")
data class GameScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameMode: String, // "WORD_GAME", "PROVERB", "DAILY_CHALLENGE", "SPEED_LEAGUE"
    val levelOrStage: Int,
    val score: Int,
    val coinsEarned: Int,
    val gemsEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)
