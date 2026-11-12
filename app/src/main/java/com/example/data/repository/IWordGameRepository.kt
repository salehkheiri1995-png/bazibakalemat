package com.example.data.repository

import com.example.data.local.db.entities.GameScoreEntity
import com.example.data.local.db.entities.LevelEntity
import com.example.data.local.db.entities.PlayerProgressEntity
import com.example.data.model.GameLevel
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface abstracting all word game data operations.
 * Enforces clean separation between UI/ViewModel and persistent SQLite Room storage.
 */
interface IWordGameRepository {

    // --- Observable Level State ---
    val allLevels: Flow<List<LevelEntity>>

    // --- Observable Progress & Metrics ---
    val allProgress: Flow<List<PlayerProgressEntity>>
    val totalStars: Flow<Int>
    val completedLevelsCount: Flow<Int>
    val highestUnlockedLevel: Flow<Int?>

    // --- Observable Scores ---
    val recentScores: Flow<List<GameScoreEntity>>
    val totalCumulativeScore: Flow<Int>
    val totalCoinsEarned: Flow<Int>

    // --- Initialization & Seeding ---
    suspend fun initializeDatabaseIfEmpty(defaultLevels: List<GameLevel>)

    // --- Level Operations ---
    fun getLevelById(levelId: Int): Flow<LevelEntity?>
    suspend fun getLevelByIdSync(levelId: Int): LevelEntity?
    fun getLevelsByCity(cityId: String): Flow<List<LevelEntity>>

    // --- Player Progress Operations ---
    fun getProgressForLevel(levelId: Int): Flow<PlayerProgressEntity?>
    suspend fun getProgressForLevelSync(levelId: Int): PlayerProgressEntity?
    fun getLevelWithProgress(levelId: Int): Flow<Pair<LevelEntity?, PlayerProgressEntity?>>
    suspend fun saveFoundWord(levelId: Int, word: String): Result<Unit>
    suspend fun completeLevel(
        levelId: Int,
        stars: Int,
        score: Int,
        coinsEarned: Int,
        gemsEarned: Int,
        totalLevelsCount: Int
    ): Result<Unit>
    suspend fun unlockLevel(levelId: Int): Result<Unit>

    // --- Scoring Operations ---
    suspend fun recordScore(
        gameMode: String,
        levelOrStage: Int,
        score: Int,
        coinsEarned: Int,
        gemsEarned: Int
    ): Result<Long>

    fun getHighScoreForMode(gameMode: String): Flow<Int?>
    fun getScoresByMode(gameMode: String): Flow<List<GameScoreEntity>>
}
