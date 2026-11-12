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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aistudio.qandparsi.data.model.DailyChallenge
import com.aistudio.qandparsi.ui.components.TopBarHeader
import com.aistudio.qandparsi.ui.theme.CoinYellow
import com.aistudio.qandparsi.ui.theme.GemBlue
import com.aistudio.qandparsi.ui.theme.GoldDark
import com.aistudio.qandparsi.ui.theme.GoldSecondary
import com.aistudio.qandparsi.ui.theme.PersianTealPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyChallengeScreen(
    challenge: DailyChallenge,
    dailyStreak: Int,
    coins: Int,
    gems: Int,
    bonusWordsCount: Int,
    isSoundEnabled: Boolean,
    selectedIndices: List<Int>,
    foundWords: Set<String>,
    isWon: Boolean,
    onToggleSound: () -> Unit,
    onSelectLetter: (Int) -> Unit,
    onClearSelection: () -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onOpenStore: () -> Unit
) {
    val scrollState = rememberScrollState()
    val selectedLetters = selectedIndices.mapNotNull { challenge.letters.getOrNull(it) }

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
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header streak badge
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "استریک روزانه: $dailyStreak روز متوالی",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 7-Day Streak Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (day in 1..7) {
                            val isReached = day <= dailyStreak
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isReached) Color(0xFFFF5722) else MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isReached) "✓" else "$day",
                                        color = if (isReached) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "روز $day",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Today's Challenge Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = challenge.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "تاریخ: ${challenge.dateString} | جایزه: ${challenge.rewardCoins} سکه + ${challenge.rewardGems} الماس",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Target Words slots
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                challenge.targetWords.forEach { word ->
                    val isFound = foundWords.contains(word)
                    Surface(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = if (isFound) Color(0xFF00796B) else MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isFound) GoldSecondary else Color(0xFF475569))
                    ) {
                        Text(
                            text = if (isFound) word else "• ".repeat(word.length).trim(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFound) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Formed Word Display Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, GoldSecondary, RoundedCornerShape(25.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClearSelection,
                        enabled = selectedLetters.isNotEmpty(),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "پاک کردن",
                            tint = if (selectedLetters.isNotEmpty()) Color(0xFFEF4444) else Color.Transparent
                        )
                    }

                    Text(
                        text = if (selectedLetters.isEmpty()) "حروف را انتخاب کنید" else selectedLetters.joinToString(""),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedLetters.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else GoldSecondary
                    )

                    IconButton(
                        onClick = onSubmit,
                        enabled = selectedLetters.isNotEmpty(),
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (selectedLetters.isNotEmpty()) Color(0xFF10B981) else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "ثبت",
                            tint = if (selectedLetters.isNotEmpty()) Color.White else Color.Transparent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Letter Buttons
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                challenge.letters.forEachIndexed { idx, char ->
                    val isUsed = selectedIndices.contains(idx)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                if (isUsed) {
                                    Brush.verticalGradient(listOf(Color(0xFFCBD5E1), Color(0xFF94A3B8)))
                                } else {
                                    Brush.verticalGradient(listOf(Color(0xFFFDE68A), Color(0xFFF59E0B)))
                                }
                            )
                            .border(1.5.dp, if (isUsed) Color(0xFF64748B) else Color(0xFFD97706), CircleShape)
                            .clickable(enabled = !isUsed) { onSelectLetter(idx) }
                            .testTag("daily_letter_$idx"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }
                }
            }

            if (isWon) {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00796B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🏆 چالش امروز با موفقیت کامل شد!", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "+${challenge.rewardCoins} سکه و +${challenge.rewardGems} الماس به حساب شما واریز شد", fontSize = 12.sp, color = Color(0xFFFDE68A))
                    }
                }
            }
        }
    }
}
