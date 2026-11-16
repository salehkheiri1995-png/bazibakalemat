package com.aistudio.qandparsi.data.repository

import com.aistudio.qandparsi.data.model.ProverbLevel
import com.aistudio.qandparsi.util.LetterScrambler

/**
 * Builds [ProverbLevel] entries from compact literals so the proverb catalogue can hold
 * hundreds of proverbs without repeating the masked text and a hand-shuffled letter bank.
 *
 * The letter bank always contains the answer plus four filler letters and is scrambled by
 * [LetterScrambler], which guarantees the answer is not spelled out contiguously.
 */
internal object ProverbFactory {

    private var counter = 0

    private val fillers = listOf(
        'ا', 'ر', 'س', 'ن', 'م', 'ک', 'ت', 'ب', 'د', 'ل', 'ه', 'ی', 'و', 'ز', 'ف', 'ق'
    )

    fun next(full: String, missing: String, meaning: String, emoji: String): ProverbLevel {
        counter += 1
        val id = counter
        val answerLetters = missing.filterNot { it.isWhitespace() }
        val bank = answerLetters.toMutableList()
        var fillerIndex = id
        while (bank.size < answerLetters.length + 4) {
            bank.add(fillers[fillerIndex % fillers.size])
            fillerIndex += 3
        }
        return ProverbLevel(
            id = id,
            proverbFull = full,
            maskedProverb = mask(full, missing),
            missingWord = missing,
            meaning = meaning,
            emojiClue = emoji,
            scrambledLetters = LetterScrambler.scramble(bank, listOf(missing), id),
            rewardCoins = 60
        )
    }

    /**
     * Hides every standalone occurrence of [missing]. A plain `replaceFirst` left a second
     * copy visible for proverbs that repeat the answer (e.g. «نمک … نمک»), which gave the
     * puzzle away. When the answer is glued inside a compound word (no standalone token),
     * we fall back to masking the first raw occurrence.
     */
    private fun mask(full: String, missing: String): String {
        val tokens = full.split(' ')
        var replacedAny = false
        val masked = tokens.map { token ->
            if (token == missing) {
                replacedAny = true
                "..."
            } else {
                token
            }
        }
        return if (replacedAny) masked.joinToString(" ") else full.replaceFirst(missing, "...")
    }
}
