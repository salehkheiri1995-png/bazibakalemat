package com.aistudio.qandparsi.data.repository

import com.aistudio.qandparsi.data.model.ProverbLevel

/**
 * The full proverb catalogue used by the proverb game. Entries are authored in compact
 * data files ([ProverbDataPart1], …) and built by [ProverbFactory].
 */
object ProverbCatalog {
    // Order matters: the ids are assigned by ProverbFactory while each part is evaluated,
    // so the parts must always be concatenated in ascending order.
    val proverbs: List<ProverbLevel> = proverbsPart1 + proverbsPart2 + proverbsPart3 +
        proverbsPart4 + proverbsPart5
}
