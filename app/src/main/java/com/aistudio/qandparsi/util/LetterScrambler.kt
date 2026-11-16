package com.aistudio.qandparsi.util

import kotlin.random.Random

/**
 * Deterministic letter-bank scrambling.
 *
 * The hand-written data used to store the letters of a word in their exact spelling order,
 * so the first letters of the bank literally spelled the answer. [scramble] guarantees that
 * the shuffled order never contains a target word as a contiguous run of letters (when a
 * non-leaking order exists).
 */
object LetterScrambler {

    fun scramble(letters: List<Char>, targetWords: List<String>, seed: Int): List<Char> {
        if (letters.size < 2) return letters
        val normalizedTargets = targetWords.map(PersianText::normalize).filter { it.isNotEmpty() }
        val original = letters.joinToString("")
        for (attempt in 0 until 80) {
            val candidate = letters.shuffled(Random(seed * 131 + attempt))
            val joined = PersianText.normalize(candidate.joinToString(""))
            val leaksAnswer = normalizedTargets.any { target -> joined.contains(target) }
            if (candidate.joinToString("") != original && !leaksAnswer) return candidate
        }
        // Last resort: reverse order, then rotate until it no longer starts with an answer.
        val reversed = letters.reversed().toMutableList()
        val joined = PersianText.normalize(reversed.joinToString(""))
        if (normalizedTargets.none { joined.contains(it) }) return reversed
        return letters.sortedByDescending { it }
    }
}
