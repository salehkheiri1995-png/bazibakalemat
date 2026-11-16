package com.aistudio.qandparsi.data.repository

import com.aistudio.qandparsi.data.model.GameLevel

/**
 * Builds the full adventure map: [CityCatalog.cities] × [CityCatalog.STAGES_PER_CITY].
 *
 * The seven cities that used to ship hand-written levels keep them as their first stages
 * (so the carefully authored word sets survive) and get generated stages for the rest.
 * Ids are assigned sequentially because the ViewModel uses `id` as the unlock index.
 */
object LevelCatalog {

    fun build(handcrafted: List<GameLevel>): List<GameLevel> {
        val handcraftedByCity = handcrafted
            .groupBy { it.city }
            .mapValues { (_, levels) -> levels.sortedBy { it.stageNumber } }

        val stagesPerCity = CityCatalog.STAGES_PER_CITY
        val result = ArrayList<GameLevel>(CityCatalog.cities.size * stagesPerCity)
        var id = 0

        CityCatalog.cities.forEach { city ->
            val custom = handcraftedByCity[city.key].orEmpty()
            for (index in 0 until stagesPerCity) {
                id += 1
                val cityStage = index + 1
                val template = LevelTemplates.templates[index % LevelTemplates.templates.size]
                val handcraftedLevel = custom.getOrNull(index)
                val stageTitle = handcraftedLevel
                    ?.cityNameFa
                    ?.substringAfter(" - ", "")
                    ?.takeIf { it.isNotBlank() }
                    ?: LevelTemplates.stageTitles[index % LevelTemplates.stageTitles.size]

                result += GameLevel(
                    id = id,
                    city = city.key,
                    cityNameFa = "${city.nameFa} - $stageTitle",
                    stageNumber = id,
                    letters = handcraftedLevel?.letters ?: template.letters,
                    targetWords = handcraftedLevel?.targetWords ?: template.targetWords,
                    bonusWords = handcraftedLevel?.bonusWords ?: template.bonusWords,
                    rewardCoins = 40 + cityStage * 5,
                    rewardGems = if (cityStage == stagesPerCity) 2 else 1,
                    cityStageNumber = cityStage,
                    cityStageCount = stagesPerCity
                )
            }
        }
        return result
    }
}
