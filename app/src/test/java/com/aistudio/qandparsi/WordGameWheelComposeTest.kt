package com.aistudio.qandparsi

import android.graphics.Bitmap
import android.view.View
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.LayoutDirection
import com.aistudio.qandparsi.data.repository.GameRepository
import com.aistudio.qandparsi.ui.screens.BazaarStoreScreen
import com.aistudio.qandparsi.ui.screens.CityMapScreen
import com.aistudio.qandparsi.ui.screens.FortuneWheelScreen
import com.aistudio.qandparsi.ui.screens.HomeScreen
import com.aistudio.qandparsi.ui.screens.ProverbScreen
import com.aistudio.qandparsi.ui.screens.WordGameScreen
import com.aistudio.qandparsi.ui.theme.MyApplicationTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.io.FileOutputStream

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w412dp-h915dp-xhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class WordGameWheelComposeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rtlWheel_visualPositionsAndDragLeftVsRight_selectExactVisualNodes() {
        val letters = listOf('س', 'ل', 'ا', 'م') // 0: Top, 1: Right, 2: Bottom, 3: Left
        val level = GameRepository.levels.first()
        val submittedWords = mutableListOf<String>()

        composeRule.setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val selectedIndices = remember { mutableStateListOf<Int>() }
                    var foundWords by remember { mutableStateOf(emptySet<String>()) }

                    WordGameScreen(
                        level = level,
                        shuffledLetters = letters,
                        selectedLetters = selectedIndices.map { letters[it] },
                        selectedLetterIndices = selectedIndices.toList(),
                        foundWords = foundWords,
                        revealedIndices = emptyMap(),
                        isLevelWon = false,
                        coins = 250,
                        gems = 12,
                        bonusWordsCount = 2,
                        freeLetterHints = 1,
                        freeWordHints = 1,
                        isSoundEnabled = false,
                        onToggleSound = {},
                        onLetterTapped = {},
                        onNodeSelected = { idx ->
                            if (!selectedIndices.contains(idx)) {
                                selectedIndices.add(idx)
                            }
                        },
                        onBacktrackNode = {
                            if (selectedIndices.isNotEmpty()) {
                                selectedIndices.removeAt(selectedIndices.lastIndex)
                            }
                        },
                        onClearSelected = { selectedIndices.clear() },
                        onShuffle = {},
                        onSubmitWord = {
                            val word = selectedIndices.map { letters[it] }.joinToString("")
                            submittedWords.add(word)
                            if (level.targetWords.contains(word)) {
                                foundWords = foundWords + word
                            }
                            selectedIndices.clear()
                        },
                        onHintLetter = {},
                        onHintWord = {},
                        onNextLevel = {},
                        onBack = {},
                        onOpenStore = {},
                        onSmashPiggyBank = {}
                    )
                }
            }
        }

        val node0Bounds = composeRule.onNodeWithTag("letter_node_0").fetchSemanticsNode().boundsInRoot
        val node1Bounds = composeRule.onNodeWithTag("letter_node_1").fetchSemanticsNode().boundsInRoot
        val node2Bounds = composeRule.onNodeWithTag("letter_node_2").fetchSemanticsNode().boundsInRoot
        val node3Bounds = composeRule.onNodeWithTag("letter_node_3").fetchSemanticsNode().boundsInRoot

        // Verify physical RTL screen positions:
        // node 0 is Top, node 2 is Bottom, node 3 is physically LEFT, node 1 is physically RIGHT
        assertTrue("Node 0 must be above Node 2", node0Bounds.center.y < node2Bounds.center.y)
        assertTrue(
            "Node 3 (left) must have smaller physical X than Node 1 (right), even in RTL",
            node3Bounds.center.x < node1Bounds.center.x
        )

        // Test 1: Drag from Top (node 0) to LEFT (node 3) — MUST select "سم" (0 -> 3), NOT "سل" (0 -> 1)
        composeRule.onRoot().performTouchInput {
            down(node0Bounds.center)
            moveTo(Offset((node0Bounds.center.x + node3Bounds.center.x) / 2f, (node0Bounds.center.y + node3Bounds.center.y) / 2f))
            moveTo(node3Bounds.center)
            up()
        }
        composeRule.waitForIdle()
        assertEquals(listOf("سم"), submittedWords)

        // Test 2: Drag from Top (node 0) to RIGHT (node 1) — MUST select "سل" (0 -> 1)
        submittedWords.clear()
        composeRule.onRoot().performTouchInput {
            down(node0Bounds.center)
            moveTo(Offset((node0Bounds.center.x + node1Bounds.center.x) / 2f, (node0Bounds.center.y + node1Bounds.center.y) / 2f))
            moveTo(node1Bounds.center)
            up()
        }
        composeRule.waitForIdle()
        assertEquals(listOf("سل"), submittedWords)

        // Test 3: Drag Top (0) -> Right (1) -> Bottom (2) -> Left (3) — continuous word "سلام"
        submittedWords.clear()
        composeRule.onRoot().performTouchInput {
            down(node0Bounds.center)
            moveTo(node1Bounds.center)
            moveTo(node2Bounds.center)
            moveTo(node3Bounds.center)
            up()
        }
        composeRule.waitForIdle()
        assertEquals(listOf("سلام"), submittedWords)

        // Test 4: Drag Left (3) -> Bottom (2) -> backtrack to Left (3) -> Top (0) => "مس"
        submittedWords.clear()
        composeRule.onRoot().performTouchInput {
            down(node3Bounds.center)
            moveTo(node2Bounds.center)
            moveTo(node3Bounds.center)
            moveTo(node0Bounds.center)
            up()
        }
        composeRule.waitForIdle()
        assertEquals(listOf("مس"), submittedWords)
    }

    @Test
    fun renderAppScreenshots() {
        val rootDir = File(".").canonicalFile
        val outDir = if (rootDir.name == "app") {
            File(rootDir.parentFile, "store-assets/screenshots")
        } else {
            File(rootDir, "store-assets/screenshots")
        }.apply { mkdirs() }

        lateinit var capturedRootView: View

        fun saveRootScreenshot(fileName: String) {
            composeRule.waitForIdle()
            composeRule.runOnIdle {
                val root = capturedRootView
                val width = root.width.coerceAtLeast(1)
                val height = root.height.coerceAtLeast(1)
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = android.graphics.Canvas(bitmap)
                root.draw(canvas)
                FileOutputStream(File(outDir, fileName)).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
            }
        }

        var screenIndex by mutableStateOf(0)
        val wordSelectedIndices = mutableStateListOf<Int>()
        val letters = listOf('س', 'ل', 'ا', 'م')
        val level = GameRepository.levels.first()
        val proverb = GameRepository.proverbs.first()

        composeRule.setContent {
            capturedRootView = LocalView.current.rootView
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    when (screenIndex) {
                        0 -> HomeScreen(
                            coins = 350,
                            gems = 15,
                            bonusWordsCount = 4,
                            currentLevelNumber = 3,
                            cityNameFa = "تهران - برج میلاد",
                            dailyStreak = 5,
                            isDailyBonusAvailable = true,
                            timeUntilNextBonusMillis = 0L,
                            loginBonusDay = 3,
                            freeLetterHints = 2,
                            freeWordHints = 1,
                            isSoundEnabled = true,
                            isVibrationEnabled = true,
                            onToggleSound = {},
                            onToggleVibration = {},
                            onOpenDailyBonus = {},
                            onNavigate = {},
                            onSmashPiggyBank = {}
                        )
                        1 -> WordGameScreen(
                            level = level,
                            shuffledLetters = letters,
                            selectedLetters = wordSelectedIndices.map { letters[it] },
                            selectedLetterIndices = wordSelectedIndices.toList(),
                            foundWords = setOf("سلام"),
                            revealedIndices = mapOf("مال" to setOf(0)),
                            isLevelWon = false,
                            coins = 350,
                            gems = 15,
                            bonusWordsCount = 4,
                            freeLetterHints = 2,
                            freeWordHints = 1,
                            isSoundEnabled = true,
                            onToggleSound = {},
                            onLetterTapped = {},
                            onNodeSelected = { if (!wordSelectedIndices.contains(it)) wordSelectedIndices.add(it) },
                            onBacktrackNode = { if (wordSelectedIndices.isNotEmpty()) wordSelectedIndices.removeAt(wordSelectedIndices.lastIndex) },
                            onClearSelected = { wordSelectedIndices.clear() },
                            onShuffle = {},
                            onSubmitWord = {},
                            onHintLetter = {},
                            onHintWord = {},
                            onNextLevel = {},
                            onBack = {},
                            onOpenStore = {},
                            onSmashPiggyBank = {}
                        )
                        2 -> ProverbScreen(
                            level = proverb,
                            currentLevelNumber = 1,
                            totalLevels = GameRepository.proverbs.size,
                            inputText = proverb.missingWord.take(2),
                            scrambledLetters = proverb.scrambledLetters,
                            selectedIndices = listOf(0, 2),
                            isWon = false,
                            coins = 350,
                            gems = 15,
                            bonusWordsCount = 4,
                            isSoundEnabled = true,
                            onToggleSound = {},
                            onLetterTapped = { _, _ -> },
                            onRemoveLetterAt = {},
                            onBackspace = {},
                            onShuffle = {},
                            onSubmit = {},
                            onNextLevel = {},
                            onBack = {},
                            onOpenStore = {}
                        )
                        3 -> FortuneWheelScreen(
                            coins = 350,
                            gems = 15,
                            bonusWordsCount = 4,
                            freeLetterHints = 2,
                            freeWordHints = 1,
                            isDailyFreeSpinAvailable = true,
                            timeUntilNextFreeSpinMillis = 0L,
                            isSoundEnabled = true,
                            isSpinning = false,
                            wheelRotation = 0f,
                            lastWonReward = null,
                            onToggleSound = {},
                            onSpinWheel = {},
                            onWatchAdForFreeSpin = {},
                            onDismissReward = {},
                            onBack = {},
                            onOpenStore = {}
                        )
                        4 -> CityMapScreen(
                            currentUnlockedLevel = 7,
                            coins = 350,
                            gems = 15,
                            bonusWordsCount = 4,
                            isSoundEnabled = true,
                            onToggleSound = {},
                            onSelectLevel = {},
                            onBack = {},
                            onOpenStore = {}
                        )
                        else -> BazaarStoreScreen(
                            coins = 350,
                            gems = 15,
                            bonusWordsCount = 4,
                            isSoundEnabled = true,
                            isWatchingAd = false,
                            isVip = false,
                            isAdsRemoved = false,
                            onToggleSound = {},
                            onSelectProduct = {},
                            onWatchAd = {},
                            onBack = {}
                        )
                    }
                }
            }
        }

        // 1. Home
        saveRootScreenshot("01_home.png")

        // 2. Word Game with active drag selection from top -> left -> bottom
        screenIndex = 1
        composeRule.waitForIdle()
        val node0 = composeRule.onNodeWithTag("letter_node_0").fetchSemanticsNode().boundsInRoot
        val node3 = composeRule.onNodeWithTag("letter_node_3").fetchSemanticsNode().boundsInRoot
        val node2 = composeRule.onNodeWithTag("letter_node_2").fetchSemanticsNode().boundsInRoot
        composeRule.onRoot().performTouchInput {
            down(node0.center)
            moveTo(node3.center)
            moveTo(node2.center)
        }
        saveRootScreenshot("02_word_game.png")
        composeRule.onRoot().performTouchInput { up() }

        // 3. Proverb
        screenIndex = 2
        saveRootScreenshot("03_proverb_game.png")

        // 4. Fortune Wheel
        screenIndex = 3
        saveRootScreenshot("04_fortune_wheel.png")

        // 5. City Map
        screenIndex = 4
        saveRootScreenshot("05_city_map.png")

        // 6. Bazaar Store
        screenIndex = 5
        saveRootScreenshot("06_bazaar_store.png")
    }
}
