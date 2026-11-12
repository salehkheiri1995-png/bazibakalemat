package com.aistudio.qandparsi.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aistudio.qandparsi.data.local.db.entities.PlayerProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerProgressDao {

    @Query("SELECT * FROM player_progress ORDER BY levelId ASC")
    fun getAllProgress(): Flow<List<PlayerProgressEntity>>

    @Query("SELECT * FROM player_progress WHERE levelId = :levelId LIMIT 1")
    fun getProgressForLevel(levelId: Int): Flow<PlayerProgressEntity?>

    @Query("SELECT * FROM player_progress WHERE levelId = :levelId LIMIT 1")
    suspend fun getProgressForLevelSync(levelId: Int): PlayerProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: PlayerProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgressList(progressList: List<PlayerProgressEntity>)

    @Query("UPDATE player_progress SET foundWordsRaw = :foundWords, lastPlayedTimestamp = :timestamp WHERE levelId = :levelId")
    suspend fun updateFoundWords(levelId: Int, foundWords: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE player_progress SET isCompleted = 1, starsEarned = :stars, highScore = CASE WHEN :score > highScore THEN :score ELSE highScore END, lastPlayedTimestamp = :timestamp WHERE levelId = :levelId")
    suspend fun markLevelCompleted(levelId: Int, stars: Int, score: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE player_progress SET isUnlocked = 1 WHERE levelId = :levelId")
    suspend fun unlockLevel(levelId: Int)

    @Query("SELECT COALESCE(SUM(starsEarned), 0) FROM player_progress")
    fun getTotalStars(): Flow<Int>

    @Query("SELECT COUNT(*) FROM player_progress WHERE isCompleted = 1")
    fun getCompletedLevelsCount(): Flow<Int>

    @Query("SELECT MAX(levelId) FROM player_progress WHERE isUnlocked = 1")
    fun getHighestUnlockedLevel(): Flow<Int?>

    @Query("DELETE FROM player_progress")
    suspend fun clearAllProgress()
}
