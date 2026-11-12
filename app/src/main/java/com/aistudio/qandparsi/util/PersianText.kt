package com.aistudio.qandparsi.util

/**
 * Normalisation helpers for Persian text.
 *
 * Persian text coming from keyboards, stored preferences or hand-written level data can use
 * different Unicode code points for the same letter (Arabic vs. Persian forms), optional
 * zero-width joiners and bidirectional marks. Comparing raw strings therefore produces
 * false "wrong word" results. Every comparison between a player's input and a stored word
 * must go through [normalize].
 */
object PersianText {

    private val ignored = setOf(
        '\u200B', // zero width space
        '\u200C', // ZWNJ
        '\u200D', // ZWJ
        '\u200E', // LRM
        '\u200F', // RLM
        '\u202A', '\u202B', '\u202C', '\u202D', '\u202E', // bidi embedding controls
        '\uFEFF'  // BOM
    )

    /** Returns a canonical form of [input] suitable for equality checks. */
    fun normalize(input: String): String {
        val out = StringBuilder(input.length)
        for (ch in input) {
            when {
                ch in ignored -> Unit
                ch == '\u0654' || ch == '\u0655' -> Unit // hamza above/below (combining) ignored
                ch == 'آ' || ch == 'أ' || ch == 'إ' || ch == 'ٱ' -> out.append('ا')
                ch == 'ي' || ch == 'ى' || ch == 'ئ' -> out.append('ی')
                ch == 'ك' -> out.append('ک')
                ch == 'ۀ' || ch == 'ة' -> out.append('ه')
                ch == 'ؤ' -> out.append('و')
                ch in '٠'..'٩' -> out.append(('۰' + (ch - '٠'))) // Arabic-Indic digits -> Persian
                ch.isWhitespace() -> out.append(' ')
                else -> out.append(ch)
            }
        }
        return out.toString().trim().replace(Regex(" +"), " ")
    }

    /** True when [a] and [b] represent the same word after normalisation. */
    fun matches(a: String, b: String): Boolean = normalize(a) == normalize(b)

    /** Converts Latin digits to Persian digits. */
    fun toPersianDigits(input: String): String {
        val out = StringBuilder(input.length)
        for (ch in input) {
            out.append(if (ch in '0'..'9') ('۰' + (ch - '0')) else ch)
        }
        return out.toString()
    }
}
