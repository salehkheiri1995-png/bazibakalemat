package com.aistudio.qandparsi

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.aistudio.qandparsi.data.local.db.WordGameDatabase
import com.aistudio.qandparsi.data.repository.GameRepository
import com.aistudio.qandparsi.data.repository.IWordGameRepository
import com.aistudio.qandparsi.data.repository.WordGameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  private lateinit var db: WordGameDatabase
  private lateinit var repository: IWordGameRepository

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, WordGameDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    repository = WordGameRepository(
        levelDao = db.levelDao(),
        playerProgressDao = db.playerProgressDao(),
        gameScoreDao = db.gameScoreDao(),
        ioDispatcher = Dispatchers.IO
    )
  }

  @After
  fun tearDown() {
    db.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("چرخ واژه", appName)
  }

  @Test
  fun `verify game repository levels and shop products`() {
    assertTrue(GameRepository.levels.isNotEmpty())
    assertTrue(GameRepository.proverbs.isNotEmpty())
    assertTrue(GameRepository.shopProducts.isNotEmpty())
    assertEquals(8, GameRepository.wheelRewards.size)

    val firstLevel = GameRepository.levels[0]
    assertEquals(1, firstLevel.stageNumber)
    assertTrue(firstLevel.targetWords.contains("سلام"))
  }

  @Test
  fun `verify proverb letters are scrambled and not in sequence of answer`() {
    GameRepository.proverbs.forEach { proverb ->
      val joined = proverb.scrambledLetters.joinToString("")
      assertTrue(
          "Proverb ${proverb.id} answer '${proverb.missingWord}' should not be in sequence",
          !joined.contains(proverb.missingWord)
      )
      proverb.missingWord.forEach { char ->
        assertTrue(proverb.scrambledLetters.contains(char))
      }
    }
  }

  @Test
  fun `verify room database and repository progression flow`() = runBlocking {
    // 1. Initialize levels
    repository.initializeDatabaseIfEmpty(GameRepository.levels)
    val allLevels = repository.allLevels.first()
    assertEquals(GameRepository.levels.size, allLevels.size)

    // 2. Check Level 1 is unlocked by default
    val progress1 = repository.getProgressForLevel(1).first()
    assertNotNull(progress1)
    assertTrue(progress1!!.isUnlocked)

    // 3. Save found word
    val wordResult = repository.saveFoundWord(1, "سلام")
    assertTrue(wordResult.isSuccess)
    val updatedProgress = repository.getProgressForLevel(1).first()
    assertTrue(updatedProgress!!.getFoundWordsSet().contains("سلام"))

    // 4. Complete level 1
    val completeResult = repository.completeLevel(
        levelId = 1,
        stars = 3,
        score = 150,
        coinsEarned = 50,
        gemsEarned = 1,
        totalLevelsCount = GameRepository.levels.size
    )
    assertTrue(completeResult.isSuccess)

    val completedProgress = repository.getProgressForLevel(1).first()
    assertTrue(completedProgress!!.isCompleted)
    assertEquals(3, completedProgress.starsEarned)

    // 5. Level 2 should now be unlocked
    val progress2 = repository.getProgressForLevel(2).first()
    assertNotNull(progress2)
    assertTrue(progress2!!.isUnlocked)

    // 6. Check score history recorded
    val recentScores = repository.recentScores.first()
    assertTrue(recentScores.isNotEmpty())
    assertEquals(150, recentScores[0].score)
  }

  @Test
  fun `verify concurrent thread safe score and progress operations`() = runBlocking {
    repository.initializeDatabaseIfEmpty(GameRepository.levels)

    // Concurrent saves from multiple coroutines
    val jobs = (1..5).map { idx ->
      async(Dispatchers.IO) {
        repository.recordScore(
            gameMode = "SPEED_LEAGUE",
            levelOrStage = idx,
            score = 100 * idx,
            coinsEarned = 20 * idx,
            gemsEarned = idx
        )
      }
    }
    jobs.awaitAll()

    val totalCoins = repository.totalCoinsEarned.first()
    val expectedCoins = (1..5).sumOf { 20 * it }
    assertEquals(expectedCoins, totalCoins)

    val highScore = repository.getHighScoreForMode("SPEED_LEAGUE").first()
    assertEquals(500, highScore)
  }

  @Test
  fun `verify 7-day daily login bonus rewards and grand prize`() {
    val rewards = GameRepository.dailyLoginRewards
    assertEquals(7, rewards.size)

    for (day in 1..7) {
      val reward = rewards[day - 1]
      assertEquals(day, reward.dayNumber)
      assertTrue("Day $day coins should be positive", reward.coins > 0)
    }

    val day7 = rewards.last()
    assertTrue("Day 7 should be marked as grand prize", day7.isGrandPrize)
    assertTrue("Day 7 should award gems", day7.gems >= 5)
    assertTrue("Day 7 should award free letter hints power-up", day7.freeLetterHints > 0)
    assertTrue("Day 7 should award free word hints power-up", day7.freeWordHints > 0)
  }

  @Test
  fun `verify 24-hour daily login bonus persistence and power-ups in preferences`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = com.aistudio.qandparsi.data.local.GamePreferences(context)

    prefs.loginBonusDay = 1
    assertEquals(1, prefs.loginBonusDay)

    prefs.freeLetterHints = 3
    assertEquals(3, prefs.freeLetterHints)

    prefs.freeWordHints = 1
    assertEquals(1, prefs.freeWordHints)

    val testTime = 1700000000000L
    prefs.lastLoginBonusTime = testTime
    assertEquals(testTime, prefs.lastLoginBonusTime)

    // Advance to day 2
    prefs.loginBonusDay = 2
    assertEquals(2, prefs.loginBonusDay)
  }

  @Test
  fun `verify wheel continuous letter selection backtracking and word submission`() {
    val application = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = com.aistudio.qandparsi.viewmodel.GameViewModel(application)

    // Clear any leftover selection
    viewModel.clearSelectedLetters()
    assertEquals(0, viewModel.selectedLetterIndices.value.size)
    assertEquals(0, viewModel.selectedLetters.value.size)

    // Simulate dragging across first 3 letters
    viewModel.selectWheelNodeIndex(0)
    assertEquals(1, viewModel.selectedLetterIndices.value.size)
    assertEquals(listOf(0), viewModel.selectedLetterIndices.value)

    viewModel.selectWheelNodeIndex(1)
    assertEquals(2, viewModel.selectedLetterIndices.value.size)
    assertEquals(listOf(0, 1), viewModel.selectedLetterIndices.value)

    viewModel.selectWheelNodeIndex(2)
    assertEquals(3, viewModel.selectedLetterIndices.value.size)
    assertEquals(listOf(0, 1, 2), viewModel.selectedLetterIndices.value)

    // Test backtracking (drag back to previous node)
    viewModel.backtrackWheelNode()
    assertEquals(2, viewModel.selectedLetterIndices.value.size)
    assertEquals(listOf(0, 1), viewModel.selectedLetterIndices.value)

    // Backtrack to 1 letter
    viewModel.backtrackWheelNode()
    assertEquals(1, viewModel.selectedLetterIndices.value.size)
    assertEquals(listOf(0), viewModel.selectedLetterIndices.value)

    // Backtrack to 0 (clean release)
    viewModel.backtrackWheelNode()
    assertEquals(0, viewModel.selectedLetterIndices.value.size)
    assertEquals(emptyList<Int>(), viewModel.selectedLetterIndices.value)

    // Test selecting correct word sequence for level 1 (سلام)
    val letters = viewModel.shuffledLetters.value
    val targetWord = "سلام"
    val indices = mutableListOf<Int>()
    for (char in targetWord) {
      val foundIdx = letters.indices.first { it !in indices && letters[it] == char }
      indices.add(foundIdx)
      viewModel.selectWheelNodeIndex(foundIdx)
    }

    assertEquals(targetWord, viewModel.selectedLetters.value.joinToString(""))
    viewModel.submitCurrentWord()

    assertTrue(
        "Level 1 target word should be found",
        viewModel.foundWords.value.contains(targetWord)
    )
    assertEquals(0, viewModel.selectedLetters.value.size)
  }
}
