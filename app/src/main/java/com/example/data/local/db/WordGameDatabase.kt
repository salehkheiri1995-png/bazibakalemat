package com.example.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.db.dao.GameScoreDao
import com.example.data.local.db.dao.LevelDao
import com.example.data.local.db.dao.PlayerProgressDao
import com.example.data.local.db.entities.GameScoreEntity
import com.example.data.local.db.entities.LevelEntity
import com.example.data.local.db.entities.PlayerProgressEntity

@Database(
    entities = [
        LevelEntity::class,
        PlayerProgressEntity::class,
        GameScoreEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WordGameDatabase : RoomDatabase() {

    abstract fun levelDao(): LevelDao
    abstract fun playerProgressDao(): PlayerProgressDao
    abstract fun gameScoreDao(): GameScoreDao

    companion object {
        @Volatile
        private var INSTANCE: WordGameDatabase? = null

        fun getDatabase(context: Context): WordGameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordGameDatabase::class.java,
                    "qand_parsi_word_game.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
