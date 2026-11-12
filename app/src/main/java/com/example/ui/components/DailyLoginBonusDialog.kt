package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.DailyLoginReward
import com.example.data.repository.GameRepository
import com.example.ui.theme.CoinYellow
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.PersianTealDark
import com.example.ui.theme.PersianTealPrimary

@Composable
fun DailyLoginBonusDialog(
    currentStreakDay: Int,
    isAvailable: Boolean,
    timeUntilNextMillis: Long,
    onClaim: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "daily_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .testTag("daily_login_bonus_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                // Header Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF004D40), Color(0xFF00241E))
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(32.dp)
                            .testTag("close_daily_bonus_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "بستن",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(GoldSecondary.copy(alpha = 0.25f))
                                .border(2.dp, GoldSecondary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎁", fontSize = 30.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "پاداش ورود روزانه (۲۴ ساعته)",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Text(
                            text = "هر ۲۴ ساعت یکبار وارد شوید و هدایای طلایی بگیرید!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = CoinYellow,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 7-Day Rewards Grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val rewards = GameRepository.dailyLoginRewards

                    // Days 1 to 6 in two rows of 3 items
                    val row1 = rewards.take(3)
                    val row2 = rewards.drop(3).take(3)
                    val day7 = rewards.last()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row1.forEach { item ->
                            DayRewardCard(
                                reward = item,
                                currentStreakDay = currentStreakDay,
                                isAvailable = isAvailable,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row2.forEach { item ->
                            DayRewardCard(
                                reward = item,
                                currentStreakDay = currentStreakDay,
                                isAvailable = isAvailable,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Grand Prize Day 7 Card
                    GrandPrizeCard(
                        reward = day7,
                        currentStreakDay = currentStreakDay,
                        isAvailable = isAvailable,
                        pulseScale = pulseScale
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Area
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isAvailable) {
                        // Main Claim Button
                        Button(
                            onClick = { onClaim(false) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .scale(pulseScale)
                                .testTag("claim_daily_bonus_btn"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "دریافت پاداش امروز",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                        }

                        // 2X Claim with Video Ad
                        Button(
                            onClick = { onClaim(true) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("claim_2x_daily_bonus_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00796B)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "دریافت ۲ برابـر پاداش با ویدیو بازار",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        // Already Claimed: Timer Countdown
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = "زمان",
                                    tint = GoldSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "پاداش امروز دریافت شده است! پاداش بعدی تا: ${formatRemainingTime(timeUntilNextMillis)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = "متوجه شدم (بستن)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun DayRewardCard(
    reward: DailyLoginReward,
    currentStreakDay: Int,
    isAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    val isClaimed = reward.dayNumber < currentStreakDay || (reward.dayNumber == currentStreakDay && !isAvailable)
    val isTodayReady = reward.dayNumber == currentStreakDay && isAvailable
    val isLocked = reward.dayNumber > currentStreakDay

    Card(
        modifier = modifier
            .height(115.dp)
            .border(
                width = if (isTodayReady) 2.dp else 1.dp,
                color = when {
                    isTodayReady -> GoldSecondary
                    isClaimed -> Color(0xFF10B981)
                    else -> Color(0xFFE2E8F0).copy(alpha = 0.2f)
                },
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isTodayReady -> GoldSecondary.copy(alpha = 0.15f)
                isClaimed -> Color(0xFF10B981).copy(alpha = 0.12f)
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Day Badge
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = when {
                    isTodayReady -> GoldSecondary
                    isClaimed -> Color(0xFF10B981)
                    else -> Color(0xFF64748B)
                }
            ) {
                Text(
                    text = reward.dayTitleFa,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isTodayReady) Color.Black else Color.White,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            // Emoji / Icon
            Text(
                text = reward.iconEmoji,
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            // Reward details text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${reward.coins} سکه",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (reward.gems > 0) {
                    Text(
                        text = "+${reward.gems} الماس",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7)
                    )
                } else if (reward.freeLetterHints > 0) {
                    Text(
                        text = "+${reward.freeLetterHints} راهنما",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                } else if (reward.freeWordHints > 0) {
                    Text(
                        text = "+۱ کلمه",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B5CF6)
                    )
                }
            }

            // Status label
            Text(
                text = when {
                    isClaimed -> "دریافت شد ✓"
                    isTodayReady -> "آماده!"
                    else -> "قفل 🔒"
                },
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    isClaimed -> Color(0xFF10B981)
                    isTodayReady -> GoldDark
                    else -> Color(0xFF94A3B8)
                }
            )
        }
    }
}

@Composable
private fun GrandPrizeCard(
    reward: DailyLoginReward,
    currentStreakDay: Int,
    isAvailable: Boolean,
    pulseScale: Float
) {
    val isClaimed = reward.dayNumber < currentStreakDay || (reward.dayNumber == currentStreakDay && !isAvailable)
    val isTodayReady = reward.dayNumber == currentStreakDay && isAvailable

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .scale(if (isTodayReady) pulseScale else 1f)
            .border(
                width = 2.dp,
                color = if (isTodayReady) GoldSecondary else Color(0xFFD97706).copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF78350F),
                            Color(0xFFB45309),
                            Color(0xFF92400E)
                        )
                    )
                )
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Crown Emoji
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(GoldSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👑", fontSize = 26.sp)
                }

                // Description
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "روز هفتم - صندوقچه شاهانه",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "${reward.coins} سکه + ${reward.gems} الماس + ۳ راهنما + ۱ کلمه کامل",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoinYellow
                    )
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        isClaimed -> Color(0xFF10B981)
                        isTodayReady -> GoldSecondary
                        else -> Color(0xFF451A03)
                    }
                ) {
                    Text(
                        text = when {
                            isClaimed -> "دریافت شد ✓"
                            isTodayReady -> "امروز!"
                            else -> "قفل 🔒"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isTodayReady) Color.Black else Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

private fun formatRemainingTime(millis: Long): String {
    if (millis <= 0) return "آماده"
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
