package com.example.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.db.entities.LevelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelDao {

    @Query("SELECT * FROM game_levels ORDER BY stageNumber ASC")
    fun getAllLevels(): Flow<List<LevelEntity>>

    @Query("SELECT * FROM game_levels WHERE levelId = :levelId LIMIT 1")
    fun getLevelById(levelId: Int): Flow<LevelEntity?>

    @Query("SELECT * FROM game_levels WHERE levelId = :levelId LIMIT 1")
    suspend fun getLevelByIdSync(levelId: Int): LevelEntity?

    @Query("SELECT * FROM game_levels WHERE cityId = :cityId ORDER BY stageNumber ASC")
    fun getLevelsByCity(cityId: String): Flow<List<LevelEntity>>

    @Query("SELECT COUNT(*) FROM game_levels")
    suspend fun getLevelsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevels(levels: List<LevelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevel(level: LevelEntity)
}
