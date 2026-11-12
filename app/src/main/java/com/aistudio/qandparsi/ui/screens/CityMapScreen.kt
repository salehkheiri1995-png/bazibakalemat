package com.aistudio.qandparsi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aistudio.qandparsi.data.model.GameLevel
import com.aistudio.qandparsi.data.repository.GameRepository
import com.aistudio.qandparsi.ui.components.TopBarHeader
import com.aistudio.qandparsi.ui.theme.GoldSecondary
import com.aistudio.qandparsi.ui.theme.PersianTealPrimary

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
    onOpenStore: () -> Unit
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
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
                        text = "نقشه ماجراجویی در شهرهای ایران",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "مرحله دلخواه خود را انتخاب کنید و واژه‌سازی را ادامه دهید",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Group levels by city
            val cityGroups = GameRepository.levels.groupBy { it.cityNameFa.substringBefore(" -") }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("city_map_list"),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cityGroups.entries.toList()) { (cityName, levels) ->
                    CitySectionCard(
                        cityName = cityName,
                        levels = levels,
                        currentUnlockedLevel = currentUnlockedLevel,
                        onSelectLevel = onSelectLevel
                    )
                }
            }
        }
    }
}

@Composable
fun CitySectionCard(
    cityName: String,
    levels: List<GameLevel>,
    currentUnlockedLevel: Int,
    onSelectLevel: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = "📍 $cityName",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                levels.forEach { level ->
                    val isUnlocked = level.stageNumber <= currentUnlockedLevel
                    val isCurrent = level.stageNumber == currentUnlockedLevel

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when {
                                    isCurrent -> PersianTealPrimary
                                    isUnlocked -> MaterialTheme.colorScheme.surfaceVariant
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                }
                            )
                            .then(
                                if (isCurrent) Modifier.border(2.dp, GoldSecondary, RoundedCornerShape(12.dp))
                                else Modifier
                            )
                            .clickable(enabled = isUnlocked) { onSelectLevel(level.stageNumber) }
                            .testTag("stage_map_btn_${level.stageNumber}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (isUnlocked) {
                                Text(
                                    text = "${level.stageNumber}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Row {
                                    repeat(if (level.stageNumber < currentUnlockedLevel) 3 else 1) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
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
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
