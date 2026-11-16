package com.aistudio.qandparsi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aistudio.qandparsi.GameConfig
import com.aistudio.qandparsi.data.model.GameLevel
import com.aistudio.qandparsi.data.repository.CityCatalog
import com.aistudio.qandparsi.data.repository.CityDefinition
import com.aistudio.qandparsi.data.repository.GameRepository
import com.aistudio.qandparsi.data.repository.StoryCatalog
import com.aistudio.qandparsi.ui.components.TopBarHeader
import com.aistudio.qandparsi.ui.theme.GoldSecondary
import com.aistudio.qandparsi.ui.theme.PersianTealPrimary
import com.aistudio.qandparsi.util.PersianText

/**
 * The 150-city map of «راز طومار هفت اقلیم»: every city holds 20 stages, a secret chest and
 * four diary pages, and completing all 20 stages hangs a golden plaque in the hall of fame.
 */
@Composable
fun CityMapScreen(
    currentUnlockedLevel: Int,
    coins: Int,
    gems: Int,
    bonusWordsCount: Int,
    isSoundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onSelectLevel: (Int) -> Unit,
    onBack: () -> Unit,
    onOpenStore: () -> Unit,
    openedChests: Set<String> = emptySet(),
    onOpenCityChest: (String) -> Unit = {},
    isLevelWon: Boolean = false,
    currentLevelId: Int = currentUnlockedLevel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBarHeader(
            coins = coins,
            gems = gems,
            bonusWordsCount = bonusWordsCount,
            isSoundEnabled = isSoundEnabled,
            onToggleSound = onToggleSound,
            onOpenStore = onOpenStore,
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("city_map_list"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MapHeaderCard(
                    currentUnlockedLevel = currentUnlockedLevel,
                    isLevelWon = isLevelWon,
                    currentLevelId = currentLevelId
                )
            }

            items(CityCatalog.cities, key = { it.key }) { city ->
                val levels = GameRepository.levelsByCity[city.key].orEmpty()
                val completed = cityCompleted(city.key, levels, currentUnlockedLevel, isLevelWon, currentLevelId)
                CitySectionCard(
                    city = city,
                    levels = levels,
                    currentUnlockedLevel = currentUnlockedLevel,
                    completedStages = completedStageCount(levels, currentUnlockedLevel, isLevelWon, currentLevelId),
                    isCityCompleted = completed,
                    chestOpened = openedChests.contains(city.key),
                    onSelectLevel = onSelectLevel,
                    onOpenChest = { onOpenCityChest(city.key) }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun MapHeaderCard(
    currentUnlockedLevel: Int,
    isLevelWon: Boolean,
    currentLevelId: Int
) {
    val totalCities = CityCatalog.cities.size
    val completedCities = CityCatalog.cities.count { city ->
        cityCompleted(
            city.key,
            GameRepository.levelsByCity[city.key].orEmpty(),
            currentUnlockedLevel,
            isLevelWon,
            currentLevelId
        )
    }
    val solvedStages = (currentUnlockedLevel - 1).coerceIn(0, GameRepository.levels.size)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🗺️ راز طومار هفت اقلیم",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = GoldSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "۱۵۰ شهر، ۳۰۰۰ مرحله و ۶۰۰ برگ از دیوان گمشده",
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🏙️ ${PersianText.toPersianDigits("$completedCities")} از " +
                    "${PersianText.toPersianDigits("$totalCities")} شهر فتح شده • " +
                    "🎯 ${PersianText.toPersianDigits("$solvedStages")} مرحله",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CitySectionCard(
    city: CityDefinition,
    levels: List<GameLevel>,
    currentUnlockedLevel: Int,
    completedStages: Int,
    isCityCompleted: Boolean,
    chestOpened: Boolean,
    onSelectLevel: (Int) -> Unit,
    onOpenChest: () -> Unit
) {
    val stageCount = levels.size.coerceAtLeast(CityCatalog.STAGES_PER_CITY)
    val storyPages = completedStages / 5
    val cityUnlocked = levels.firstOrNull()?.let { it.id <= currentUnlockedLevel } ?: false
    val chestEnabled = cityUnlocked && !chestOpened

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("city_card_${city.key}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${city.emoji} ${city.nameFa}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = city.provinceFa,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${PersianText.toPersianDigits("$completedStages")} از " +
                        PersianText.toPersianDigits("$stageCount"),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PersianTealPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { (completedStages.toFloat() / stageCount).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PersianTealPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                if (isCityCompleted) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "🏅", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
                maxItemsInEachRow = 5
            ) {
                levels.forEach { level ->
                    StageButton(
                        level = level,
                        isUnlocked = level.id <= currentUnlockedLevel,
                        isCurrent = level.id == currentUnlockedLevel,
                        isCompleted = level.id < currentUnlockedLevel,
                        onClick = { onSelectLevel(level.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📜 ${PersianText.toPersianDigits("$storyPages")} از " +
                        PersianText.toPersianDigits("${CityCatalog.STAGES_PER_CITY / 5}") + " برگ طومار",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.weight(1f))
                ChestButton(
                    cityKey = city.key,
                    opened = chestOpened,
                    enabled = chestEnabled,
                    unlocked = cityUnlocked,
                    onClick = onOpenChest
                )
            }

            if (chestOpened) {
                Spacer(modifier = Modifier.height(6.dp))
                val souvenir = StoryCatalog.souvenirFor(city.key)
                Text(
                    text = "${souvenir.second} سوغات کهن: ${souvenir.first}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PersianTealPrimary
                )
            }

            if (isCityCompleted) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🏅 لوح طلایی ${city.nameFa} در تالار افتخارات نصب شد",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldSecondary
                )
            }
        }
    }
}

@Composable
private fun StageButton(
    level: GameLevel,
    isUnlocked: Boolean,
    isCurrent: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isCurrent -> PersianTealPrimary
                    isCompleted -> MaterialTheme.colorScheme.surfaceVariant
                    isUnlocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                }
            )
            .then(
                if (isCurrent) {
                    Modifier.border(2.dp, GoldSecondary, RoundedCornerShape(12.dp))
                } else {
                    Modifier
                }
            )
            .clickable(enabled = isUnlocked || isCurrent, onClick = onClick)
            .testTag("stage_map_btn_${level.id}"),
        contentAlignment = Alignment.Center
    ) {
        if (isUnlocked || isCurrent) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = PersianText.toPersianDigits("${level.cityStageNumber}"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                )
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "کامل شده",
                        tint = GoldSecondary,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        } else {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "قفل",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

@Composable
private fun ChestButton(
    cityKey: String,
    opened: Boolean,
    enabled: Boolean,
    unlocked: Boolean,
    onClick: () -> Unit
) {
    val label = when {
        opened -> "✅ صندوق باز شد"
        !unlocked -> "🔒 صندوق مخفی"
        else -> "🎁 صندوق (${PersianText.toPersianDigits("${GameConfig.SECRET_CHEST_GEM_COST}")} 💎)"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (enabled) GoldSecondary.copy(alpha = 0.22f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("city_chest_$cityKey")
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (enabled) GoldSecondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
        )
    }
}

/** Stages of [levels] that are fully cleared (the last one of the map has no next id). */
private fun completedStageCount(
    levels: List<GameLevel>,
    currentUnlockedLevel: Int,
    isLevelWon: Boolean,
    currentLevelId: Int
): Int {
    if (levels.isEmpty()) return 0
    val lastId = levels.last().id
    if (currentUnlockedLevel > lastId) return levels.size
    if (currentUnlockedLevel == lastId && isLevelWon && currentLevelId == lastId) return levels.size
    return levels.count { it.id < currentUnlockedLevel }
}

private fun cityCompleted(
    cityKey: String,
    levels: List<GameLevel>,
    currentUnlockedLevel: Int,
    isLevelWon: Boolean,
    currentLevelId: Int
): Boolean {
    if (levels.isEmpty()) return false
    return completedStageCount(levels, currentUnlockedLevel, isLevelWon, currentLevelId) == levels.size
}
