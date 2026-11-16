package com.aistudio.qandparsi

import com.aistudio.qandparsi.data.repository.CityCatalog
import com.aistudio.qandparsi.data.repository.GameRepository
import com.aistudio.qandparsi.data.repository.StoryCatalog
import com.aistudio.qandparsi.util.PersianText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Guards the hand-written level data: every target/bonus word must be formable from the
 * level letters and the proverb answers must be formable from their scrambled letters.
 */
class GameDataValidationTest {

    @Test
    fun mapHasEveryCityAndStageInOrder() {
        assertEquals("Expected 150 cities", 150, CityCatalog.cities.size)
        assertEquals(
            "Every city needs a unique key",
            CityCatalog.cities.size,
            CityCatalog.cities.map { it.key }.toSet().size
        )
        assertEquals(
            "Expected 150 cities × 20 stages",
            CityCatalog.cities.size * CityCatalog.STAGES_PER_CITY,
            GameRepository.levels.size
        )
        assertEquals(
            "Level ids must stay sequential because the ViewModel uses them as indexes",
            GameRepository.levels.size,
            GameRepository.levels.map { it.id }.toSet().size
        )

        val byCity = GameRepository.levels.groupBy { it.city }
        assertEquals(
            "Every catalogued city must be playable",
            CityCatalog.cities.map { it.key }.toSet(),
            byCity.keys
        )
        CityCatalog.cities.forEachIndexed { cityIndex, city ->
            val cityLevels = byCity.getValue(city.key)
            assertEquals(
                "${city.nameFa} must have ${CityCatalog.STAGES_PER_CITY} stages",
                CityCatalog.STAGES_PER_CITY,
                cityLevels.size
            )
            cityLevels.forEachIndexed { stageIndex, level ->
                val expectedId = cityIndex * CityCatalog.STAGES_PER_CITY + stageIndex + 1
                assertEquals(
                    "${city.nameFa} stage ${stageIndex + 1} has an out-of-order id",
                    expectedId,
                    level.id
                )
                assertEquals(
                    "${city.nameFa} stage ${stageIndex + 1} has a wrong stage number",
                    stageIndex + 1,
                    level.cityStageNumber
                )
                assertEquals(
                    "${city.nameFa}: all stages must share the same stage count",
                    CityCatalog.STAGES_PER_CITY,
                    level.cityStageCount
                )
            }
        }
    }

    @Test
    fun storyMilestonesFollowCityStages() {
        CityCatalog.cities.forEach { city ->
            val cityLevels = GameRepository.levelsByCity.getValue(city.key)
            cityLevels.forEach { level ->
                val unlock = StoryCatalog.unlockFor(level)
                if (level.cityStageNumber % 5 == 0) {
                    assertNotNull(
                        "${city.nameFa} stage ${level.cityStageNumber} must unlock a diary page",
                        unlock
                    )
                    assertTrue(
                        "${city.nameFa}: the diary page must mention the city",
                        unlock!!.page.body.contains(city.nameFa)
                    )
                    assertTrue(
                        "${city.nameFa}: every diary page needs a souvenir",
                        unlock.page.souvenirName.isNotBlank()
                    )
                } else {
                    assertNull(
                        "${city.nameFa} stage ${level.cityStageNumber} must not unlock a page",
                        unlock
                    )
                }

                if (level.cityStageNumber == CityCatalog.STAGES_PER_CITY) {
                    assertNotNull(
                        "${city.nameFa}: finishing the city must award a golden plaque",
                        unlock?.plaqueText
                    )
                } else {
                    assertNull(
                        "${city.nameFa}: the plaque may only be awarded on the last stage",
                        unlock?.plaqueText
                    )
                }
            }

            val souvenir = StoryCatalog.souvenirFor(city.key)
            assertTrue("${city.nameFa}: chest souvenir is missing", souvenir.first.isNotBlank())
        }
    }

    @Test
    fun levelsHaveUniqueIdsAndFormableWords() {
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
    fun proverbCatalogueIsLargeAndUnique() {
        val proverbs = GameRepository.proverbs
        assertTrue("Expected at least 500 proverbs, found ${proverbs.size}", proverbs.size >= 500)
        assertEquals(
            "Proverb ids must be unique (the factory counter assigns them)",
            proverbs.size,
            proverbs.map { it.id }.toSet().size
        )
        assertEquals(
            "Proverb texts must be unique",
            proverbs.size,
            proverbs.map { it.proverbFull }.toSet().size
        )
        proverbs.forEach { proverb ->
            assertTrue("Proverb ${proverb.id} has no meaning", proverb.meaning.isNotBlank())
            assertTrue("Proverb ${proverb.id} has no emoji clue", proverb.emojiClue.isNotBlank())
            assertTrue(
                "Proverb ${proverb.id}: the masked text reveals the answer '${proverb.missingWord}'",
                proverb.maskedProverb.split(' ').none { it == proverb.missingWord }
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
