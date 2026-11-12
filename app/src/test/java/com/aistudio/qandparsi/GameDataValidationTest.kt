package com.aistudio.qandparsi

import com.aistudio.qandparsi.data.repository.GameRepository
import com.aistudio.qandparsi.util.PersianText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Guards the hand-written level data: every target/bonus word must be formable from the
 * level letters and the proverb answers must be formable from their scrambled letters.
 */
class GameDataValidationTest {

    @Test
    fun levelsHaveUniqueIdsAndFormableWords() {
        assertEquals(35, GameRepository.levels.size)
        assertEquals(
            GameRepository.levels.size,
            GameRepository.levels.map { it.id }.toSet().size
        )

        GameRepository.levels.forEach { level ->
            assertTrue("Level ${level.id} has no target words", level.targetWords.isNotEmpty())

            val letters = normalizedLetterCounts(level.letters)
            (level.targetWords + level.bonusWords).forEach { word ->
                assertTrue(
                    "Level ${level.id}: '$word' cannot be formed from ${level.letters}",
                    canForm(word, letters)
                )
            }

            val allWords = (level.targetWords + level.bonusWords).map(PersianText::normalize)
            assertEquals(
                "Level ${level.id} contains duplicate words",
                allWords.size,
                allWords.toSet().size
            )
        }
    }

    @Test
    fun proverbAnswersAreFormableFromScrambledLetters() {
        GameRepository.proverbs.forEach { proverb ->
            val letters = normalizedLetterCounts(proverb.scrambledLetters)
            assertTrue(
                "Proverb ${proverb.id}: '${proverb.missingWord}' cannot be formed",
                canForm(proverb.missingWord, letters)
            )
            assertTrue(
                "Proverb ${proverb.id}: masked text must contain the placeholder",
                proverb.maskedProverb.contains("...")
            )
        }
    }

    @Test
    fun dailyChallengeIsInternallyConsistent() {
        val challenges = GameRepository.dailyChallenges
        assertTrue("Expected at least 30 daily challenges", challenges.size >= 30)
        for (challenge in challenges) {
            val letters = normalizedLetterCounts(challenge.letters)
            assertTrue("Daily '${challenge.title}' has no targets", challenge.targetWords.isNotEmpty())
            challenge.targetWords.forEach { word ->
                assertTrue("Daily '${challenge.title}': '$word' cannot be formed", canForm(word, letters))
            }
            assertEquals(
                "Daily '${challenge.title}' has duplicate targets",
                challenge.targetWords.size,
                challenge.targetWords.map(PersianText::normalize).toSet().size
            )
        }
        val today = GameRepository.todayChallenge()
        assertTrue(today.dateString.isNotBlank())
    }

    private fun normalizedLetterCounts(letters: List<Char>): Map<Char, Int> {
        val counts = mutableMapOf<Char, Int>()
        letters.forEach { raw ->
            val ch = PersianText.normalize(raw.toString()).singleOrNull() ?: raw
            counts[ch] = (counts[ch] ?: 0) + 1
        }
        return counts
    }

    private fun canForm(word: String, available: Map<Char, Int>): Boolean {
        val remaining = available.toMutableMap()
        PersianText.normalize(word).filterNot { it == ' ' }.forEach { ch ->
            val left = remaining[ch] ?: return false
            if (left <= 0) return false
            remaining[ch] = left - 1
        }
        return true
    }
}
