package com.aistudio.qandparsi

import com.aistudio.qandparsi.data.repository.GameRepository
import com.aistudio.qandparsi.util.PersianText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The daily challenge used to ship its letter bank in the exact spelling order of the first
 * answers (e.g. «سفیر» was literally the first four letters). These tests lock the fix in.
 */
class DailyChallengeScrambleTest {

    @Test
    fun todayChallengeNeverSpellsATargetContiguously() {
        val challenge = GameRepository.todayChallenge()
        val joined = PersianText.normalize(challenge.letters.joinToString(""))
        challenge.targetWords.forEach { word ->
            assertTrue(
                "Daily challenge '${challenge.title}' leaks the answer '$word' in its letter order",
                !joined.contains(PersianText.normalize(word))
            )
        }
    }

    @Test
    fun scrambleKeepsEveryLetterAndChangesTheOrder() {
        GameRepository.dailyChallenges.forEach { challenge ->
            val scrambled = GameRepository.scrambleLetters(
                letters = challenge.letters,
                targetWords = challenge.targetWords,
                seed = challenge.letters.size * 31
            )
            assertEquals(
                "Scrambling must not add or drop letters (${challenge.title})",
                challenge.letters.sorted(),
                scrambled.sorted()
            )
            assertNotEquals(
                "Scrambling must change the original order (${challenge.title})",
                challenge.letters,
                scrambled
            )
        }
    }

    @Test
    fun everyDailyChallengeCanStillBeSolved() {
        GameRepository.dailyChallenges.forEach { challenge ->
            val scrambled = GameRepository.scrambleLetters(
                letters = challenge.letters,
                targetWords = challenge.targetWords,
                seed = challenge.letters.size
            )
            val available = scrambled.groupingBy { it }.eachCount().toMutableMap()
            challenge.targetWords.forEach { word ->
                val remaining = available.toMutableMap()
                PersianText.normalize(word).forEach { ch ->
                    val left = remaining[ch] ?: 0
                    assertTrue("'$word' is no longer formable after scrambling", left > 0)
                    remaining[ch] = left - 1
                }
            }
        }
    }
}
