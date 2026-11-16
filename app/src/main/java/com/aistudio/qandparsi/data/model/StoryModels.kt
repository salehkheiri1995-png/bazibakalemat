package com.aistudio.qandparsi.data.model

/** A leaf of Hatif's diary, unlocked every five stages of a city. */
data class DiaryPage(
    val cityKey: String,
    val cityNameFa: String,
    val milestone: Int,
    val title: String,
    val body: String,
    val souvenirName: String,
    val souvenirEmoji: String
)

/** What the player just unlocked when a stage was completed. */
data class StoryUnlock(
    val page: DiaryPage,
    val cityCompleted: Boolean,
    val plaqueText: String?
)
