package com.aistudio.qandparsi.data.repository

import com.aistudio.qandparsi.data.local.db.dao.GameScoreDao
import com.aistudio.qandparsi.data.local.db.dao.LevelDao
import com.aistudio.qandparsi.data.local.db.dao.PlayerProgressDao
import com.aistudio.qandparsi.data.local.db.entities.GameScoreEntity
import com.aistudio.qandparsi.data.local.db.entities.LevelEntity
import com.aistudio.qandparsi.data.local.db.entities.PlayerProgressEntity
import com.aistudio.qandparsi.data.model.GameLevel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Production implementation of the Repository Pattern for the Word Game.
 * Ensures thread-safe execution across background coroutine dispatchers
 * and synchronization via Mutex locks to prevent race conditions.
 */
class WordGameRepository(
    private val levelDao: LevelDao,
    private val playerProgressDao: PlayerProgressDao,
    private val gameScoreDao: GameScoreDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IWordGameRepository {

    // Mutex locks to prevent race conditions during concurrent updates
    private val progressMutex = Mutex()
    private val initMutex = Mutex()

    override val allLevels: Flow<List<LevelEntity>> =
        levelDao.getAllLevels().flowOn(ioDispatcher)

    override val allProgress: Flow<List<PlayerProgressEntity>> =
        playerProgressDao.getAllProgress().flowOn(ioDispatcher)

    override val totalStars: Flow<Int> =
        playerProgressDao.getTotalStars().flowOn(ioDispatcher)

    override val completedLevelsCount: Flow<Int> =
        playerProgressDao.getCompletedLevelsCount().flowOn(ioDispatcher)

    override val highestUnlockedLevel: Flow<Int?> =
        playerProgressDao.getHighestUnlockedLevel().flowOn(ioDispatcher)

    override val recentScores: Flow<List<GameScoreEntity>> =
        gameScoreDao.getRecentScores().flowOn(ioDispatcher)

    override val totalCumulativeScore: Flow<Int> =
        gameScoreDao.getTotalCumulativeScore().flowOn(ioDispatcher)

    override val totalCoinsEarned: Flow<Int> =
        gameScoreDao.getTotalCoinsEarned().flowOn(ioDispatcher)

    /**
     * Seeds initial game levels and unlocks Level 1 on first launch.
     * Guarded with initMutex to ensure single-execution thread safety.
     */
    override suspend fun initializeDatabaseIfEmpty(defaultLevels: List<GameLevel>) = withContext(ioDispatcher) {
        initMutex.withLock {
            val count = levelDao.getLevelsCount()
            when {
                count == 0 -> {
                    levelDao.insertLevels(defaultLevels.map { it.toLevelEntity() })
                    playerProgressDao.insertProgressList(defaultLevels.map { it.toInitialProgress() })
                }
                count != defaultLevels.size -> {
                    // Map expansion (35 → 3000 levels): upsert the new levels and add the
                    // missing progress rows without touching the player's existing progress.
                    levelDao.insertLevels(defaultLevels.map { it.toLevelEntity() })
                    val knownLevelIds = playerProgressDao.getAllProgressLevelIds().toSet()
                    val missingProgress = defaultLevels
                        .filterNot { it.id in knownLevelIds }
                        .map { it.toInitialProgress() }
                    if (missingProgress.isNotEmpty()) {
                        playerProgressDao.insertProgressList(missingProgress)
                    }
                }
            }
        }
    }

    private fun GameLevel.toLevelEntity(): LevelEntity = LevelEntity(
        levelId = id,
        cityId = city,
        cityNameFa = cityNameFa,
        stageNumber = stageNumber,
        lettersRaw = letters.joinToString(","),
        targetWordsRaw = targetWords.joinToString(","),
        bonusWordsRaw = bonusWords.joinToString(","),
        rewardCoins = rewardCoins,
        rewardGems = rewardGems
    )

    private fun GameLevel.toInitialProgress(): PlayerProgressEntity = PlayerProgressEntity(
        levelId = id,
        isUnlocked = id == 1,
        isCompleted = false,
        starsEarned = 0,
        foundWordsRaw = "",
        highScore = 0
    )

    override fun getLevelById(levelId: Int): Flow<LevelEntity?> =
        levelDao.getLevelById(levelId).flowOn(ioDispatcher)

    override suspend fun getLevelByIdSync(levelId: Int): LevelEntity? = withContext(ioDispatcher) {
        levelDao.getLevelByIdSync(levelId)
    }

    override fun getLevelsByCity(cityId: String): Flow<List<LevelEntity>> =
        levelDao.getLevelsByCity(cityId).flowOn(ioDispatcher)

    override fun getProgressForLevel(levelId: Int): Flow<PlayerProgressEntity?> =
        playerProgressDao.getProgressForLevel(levelId).flowOn(ioDispatcher)

    override suspend fun getProgressForLevelSync(levelId: Int): PlayerProgressEntity? = withContext(ioDispatcher) {
        playerProgressDao.getProgressForLevelSync(levelId)
    }

    override fun getLevelWithProgress(levelId: Int): Flow<Pair<LevelEntity?, PlayerProgressEntity?>> {
        return levelDao.getLevelById(levelId)
            .combine(playerProgressDao.getProgressForLevel(levelId)) { level, progress ->
                Pair(level, progress)
            }
            .flowOn(ioDispatcher)
    }

    /**
     * Atomically records a found word in player progress.
     */
    override suspend fun saveFoundWord(levelId: Int, word: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            progressMutex.withLock {
                val progress = playerProgressDao.getProgressForLevelSync(levelId)
                val currentWords = progress?.getFoundWordsSet() ?: emptySet()
                if (!currentWords.contains(word)) {
                    val updatedWords = (currentWords + word).joinToString(",")
                    playerProgressDao.updateFoundWords(levelId, updatedWords)
                }
            }
        }
    }

    /**
     * Completes a level, persists stars, logs session score, and unlocks next stage.
     */
    override suspend fun completeLevel(
        levelId: Int,
        stars: Int,
        score: Int,
        coinsEarned: Int,
        gemsEarned: Int,
        totalLevelsCount: Int
    ): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            progressMutex.withLock {
                // 1. Mark completed
                playerProgressDao.markLevelCompleted(
                    levelId = levelId,
                    stars = stars,
                    score = score
                )

                // 2. Unlock next level
                if (levelId < totalLevelsCount) {
                    val nextLevelId = levelId + 1
                    val nextProgress = playerProgressDao.getProgressForLevelSync(nextLevelId)
                    if (nextProgress == null) {
                        playerProgressDao.insertOrUpdateProgress(
                            PlayerProgressEntity(
                                levelId = nextLevelId,
                                isUnlocked = true
                            )
                        )
                    } else {
                        playerProgressDao.unlockLevel(nextLevelId)
                    }
                }

                // 3. Log game score history
                gameScoreDao.insertScore(
                    GameScoreEntity(
                        gameMode = "WORD_GAME",
                        levelOrStage = levelId,
                        score = score,
                        coinsEarned = coinsEarned,
                        gemsEarned = gemsEarned
                    )
                )
                Unit
            }
        }
    }

    override suspend fun unlockLevel(levelId: Int): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            progressMutex.withLock {
                playerProgressDao.unlockLevel(levelId)
            }
        }
    }

    override suspend fun recordScore(
        gameMode: String,
        levelOrStage: Int,
        score: Int,
        coinsEarned: Int,
        gemsEarned: Int
    ): Result<Long> = withContext(ioDispatcher) {
        runCatching {
            gameScoreDao.insertScore(
                GameScoreEntity(
                    gameMode = gameMode,
                    levelOrStage = levelOrStage,
                    score = score,
                    coinsEarned = coinsEarned,
                    gemsEarned = gemsEarned
                )
            )
        }
    }

    override fun getHighScoreForMode(gameMode: String): Flow<Int?> =
        gameScoreDao.getHighScoreByMode(gameMode).flowOn(ioDispatcher)

    override fun getScoresByMode(gameMode: String): Flow<List<GameScoreEntity>> =
        gameScoreDao.getScoresByMode(gameMode).flowOn(ioDispatcher)
}
