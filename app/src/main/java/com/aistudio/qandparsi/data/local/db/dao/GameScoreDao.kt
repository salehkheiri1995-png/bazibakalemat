package com.aistudio.qandparsi.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aistudio.qandparsi.data.local.db.entities.GameScoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameScoreDao {

    @Query("SELECT * FROM game_scores ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentScores(limit: Int = 20): Flow<List<GameScoreEntity>>

    @Query("SELECT * FROM game_scores WHERE gameMode = :gameMode ORDER BY timestamp DESC")
    fun getScoresByMode(gameMode: String): Flow<List<GameScoreEntity>>

    @Query("SELECT MAX(score) FROM game_scores WHERE gameMode = :gameMode")
    fun getHighScoreByMode(gameMode: String): Flow<Int?>

    @Query("SELECT COALESCE(SUM(score), 0) FROM game_scores")
    fun getTotalCumulativeScore(): Flow<Int>

    @Query("SELECT COALESCE(SUM(coinsEarned), 0) FROM game_scores")
    fun getTotalCoinsEarned(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: GameScoreEntity): Long

    @Query("DELETE FROM game_scores WHERE id = :id")
    suspend fun deleteScoreById(id: Long)
}
