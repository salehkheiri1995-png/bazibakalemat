package com.aistudio.qandparsi.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.aistudio.qandparsi.data.model.WheelReward
import com.aistudio.qandparsi.data.repository.GameRepository
import com.aistudio.qandparsi.ui.components.TopBarHeader
import com.aistudio.qandparsi.ui.theme.CoinYellow
import com.aistudio.qandparsi.ui.theme.GoldDark
import com.aistudio.qandparsi.ui.theme.GoldSecondary
import com.aistudio.qandparsi.ui.theme.PersianTealDark
import com.aistudio.qandparsi.ui.theme.PersianTealPrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FortuneWheelScreen(
    coins: Int,
    gems: Int,
    bonusWordsCount: Int,
    freeLetterHints: Int,
    freeWordHints: Int,
    isDailyFreeSpinAvailable: Boolean,
    timeUntilNextFreeSpinMillis: Long,
    isSoundEnabled: Boolean,
    isSpinning: Boolean,
    wheelRotation: Float,
    lastWonReward: WheelReward?,
    onToggleSound: () -> Unit,
    onSpinWheel: () -> Unit,
    onWatchAdForFreeSpin: () -> Unit,
    onDismissReward: () -> Unit,
    onBack: () -> Unit,
    onOpenStore: () -> Unit
) {
    val scrollState = rememberScrollState()

    val animatedRotation by animateFloatAsState(
        targetValue = wheelRotation,
        animationSpec = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
        label = "wheelRotationAnim"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "wheel_effects")

    // Flapper needle vibration animation during spinning
    val flapperDeflection by infiniteTransition.animateFloat(
        initialValue = -7f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flapper_flutter"
    )

    // Running flashing bulbs around the wheel rim
    val bulbPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bulb_phase"
    )

    // Pulse animation for free spin CTA
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

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
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Info & Hints Storage
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stored powerups
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "$freeLetterHints", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "💡", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "$freeWordHints", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "🔮", fontSize = 12.sp)
                            }
                        }
                    }

                    // Title
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "گردونه شانس قند پارسی",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldSecondary
                        )
                        Text(
                            text = "روزانه ۱ بار رایگان بچرخانید و سکه و راهنما ببرید!",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Rotating UI Component with Animated Segments
            Box(
                modifier = Modifier
                    .size(316.dp)
                    .testTag("fortune_wheel_container"),
                contentAlignment = Alignment.Center
            ) {
                // Layer 1: Outer Glowing Gold Bezel & Flashing LED Lights
                Canvas(modifier = Modifier.size(316.dp)) {
                    val radius = size.minDimension / 2f
                    val center = Offset(radius, radius)

                    // Outer shadow & gold rim
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF78350F), Color(0xFF451A03)),
                            center = center,
                            radius = radius
                        ),
                        radius = radius,
                        center = center
                    )

                    drawCircle(
                        color = Color(0xFFF59E0B),
                        radius = radius - 3.dp.toPx(),
                        center = center,
                        style = Stroke(width = 5.dp.toPx())
                    )

                    // 16 Animated Flashing Bulbs around the perimeter
                    val bulbCount = 16
                    for (b in 0 until bulbCount) {
                        val angle = (2 * Math.PI * b / bulbCount)
                        val bulbRadius = radius - 7.dp.toPx()
                        val bx = center.x + (cos(angle) * bulbRadius).toFloat()
                        val by = center.y + (sin(angle) * bulbRadius).toFloat()

                        val isEven = b % 2 == 0
                        val isLit = if (bulbPhase > 0.5f) isEven else !isEven

                        val bulbColor = if (isLit) Color(0xFFFFF59D) else Color(0xFF80DEEA)
                        drawCircle(
                            color = bulbColor,
                            radius = 4.dp.toPx(),
                            center = Offset(bx, by)
                        )
                    }
                }

                // Layer 2: Rotating Wheel Slices with Persian Labels & Emojis
                Box(
                    modifier = Modifier
                        .size(292.dp)
                        .clip(CircleShape)
                        .rotate(animatedRotation)
                ) {
                    val rewards = GameRepository.wheelRewards
                    val count = rewards.size
                    val sweep = 360f / count

                    // Draw Pie Arcs and Spokes on Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasSize = size.minDimension
                        val radius = canvasSize / 2f
                        val center = Offset(radius, radius)

                        rewards.forEachIndexed { i, reward ->
                            val startAngle = i * sweep

                            // Colorful slice arc
                            drawArc(
                                color = Color(reward.colorHex),
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = true,
                                topLeft = Offset.Zero,
                                size = Size(canvasSize, canvasSize)
                            )

                            // Inner subtle radial highlight
                            drawArc(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color.White.copy(alpha = 0.25f), Color.Transparent),
                                    center = center,
                                    radius = radius
                                ),
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = true,
                                topLeft = Offset.Zero,
                                size = Size(canvasSize, canvasSize)
                            )
                        }

                        // Gold spokes / divider lines between segments
                        for (i in 0 until count) {
                            val angleRad = Math.toRadians((i * sweep).toDouble())
                            val endX = center.x + (cos(angleRad) * radius).toFloat()
                            val endY = center.y + (sin(angleRad) * radius).toFloat()
                            drawLine(
                                color = Color(0xFFFFF7C2),
                                start = center,
                                end = Offset(endX, endY),
                                strokeWidth = 2.dp.toPx()
                            )
                        }

                        // Inner circular separator
                        drawCircle(
                            color = Color(0xFFF59E0B),
                            radius = radius * 0.45f,
                            center = center,
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }

                    // Render Emojis and Persian Text on each segment
                    rewards.forEachIndexed { i, reward ->
                        val midAngle = i * sweep + (sweep / 2f)
                        val angleRad = Math.toRadians(midAngle.toDouble())
                        val textRadius = 88.0
                        val xOffset = (cos(angleRad) * textRadius).dp
                        val yOffset = (sin(angleRad) * textRadius).dp

                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                // absoluteOffset: the wheel slices are drawn on a Canvas in
                                // absolute coordinates. Direction-aware offset mirrored the
                                // labels in RTL and put each label on the wrong slice.
                                .absoluteOffset(x = xOffset, y = yOffset)
                                .rotate(midAngle + 90f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = reward.iconEmoji,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = reward.labelFa,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (reward.isJackpot) Color(0xFFFFFDE7) else Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Layer 3: Center Hub Button ("بچرخون" / SPIN)
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFFDE68A), Color(0xFFF59E0B), Color(0xFFD97706))
                            )
                        )
                        .border(3.dp, Color(0xFFFFFBEB), CircleShape)
                        .clickable(enabled = !isSpinning) { onSpinWheel() }
                        .testTag("center_hub_spin_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isSpinning) "⏳" else "بچرخون",
                            fontSize = if (isSpinning) 16.sp else 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF451A03)
                        )
                        if (!isSpinning) {
                            Text(
                                text = "SPIN",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF78350F)
                            )
                        }
                    }
                }

                // Layer 4: Mechanical Top Pointer Needle at 12 o'clock with flutter
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .absoluteOffset(y = (-4).dp)
                        .rotate(if (isSpinning) flapperDeflection else 0f)
                ) {
                    Canvas(modifier = Modifier.size(34.dp, 40.dp)) {
                        val path = Path().apply {
                            moveTo(size.width / 2f, size.height)
                            lineTo(2.dp.toPx(), 2.dp.toPx())
                            lineTo(size.width - 2.dp.toPx(), 2.dp.toPx())
                            close()
                        }
                        // Needle gradient & border
                        drawPath(
                            path = path,
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFFEF4444), Color(0xFFB91C1C))
                            )
                        )
                        drawPath(
                            path = path,
                            color = Color(0xFFFFFBEB),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }

            // Daily Free Spin Countdown Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = if (isDailyFreeSpinAvailable) Color(0xFF004D40) else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isDailyFreeSpinAvailable) GoldSecondary else MaterialTheme.colorScheme.surface
                    ) {
                        Text(
                            text = if (isDailyFreeSpinAvailable) "رایگان امروز!" else formatWheelTime(timeUntilNextFreeSpinMillis),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDailyFreeSpinAvailable) Color.Black else GoldSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isDailyFreeSpinAvailable) "چرخش رایگان ۲۴ ساعته شما آماده است!" else "چرخش روزانه مصرف شد. بعدی تا:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDailyFreeSpinAvailable) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = if (isDailyFreeSpinAvailable) Icons.Default.CardGiftcard else Icons.Default.Timer,
                            contentDescription = null,
                            tint = if (isDailyFreeSpinAvailable) GoldSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Controls & Spin Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isDailyFreeSpinAvailable) {
                    // Primary Daily Free Spin Button
                    Button(
                        onClick = onSpinWheel,
                        enabled = !isSpinning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .scale(if (!isSpinning) pulseScale else 1f)
                            .testTag("spin_wheel_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldSecondary)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isSpinning) "در حال چرخش گردونه..." else "چرخش رایگان امروز 🎁",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null, tint = Color.Black)
                        }
                    }
                } else {
                    // Secondary Spin with 50 Coins Button
                    Button(
                        onClick = onSpinWheel,
                        enabled = !isSpinning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("spin_wheel_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PersianTealPrimary)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isSpinning) "در حال چرخش..." else "چرخش مجدد (۵۰ سکه 🪙)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null, tint = Color.White)
                        }
                    }

                    // Watch Video for Free Extra Spin Button
                    OutlinedButton(
                        onClick = onWatchAdForFreeSpin,
                        enabled = !isSpinning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("free_spin_ad_btn"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "تماشای ویدیو بازار برای چرخش رایگان",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.Default.Videocam, contentDescription = null, tint = Color(0xFF00A651))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Reward Celebration Dialog
        if (lastWonReward != null) {
            Dialog(onDismissRequest = onDismissReward) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("wheel_reward_dialog"),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = if (lastWonReward.isJackpot) "🎰👑💎" else "${lastWonReward.iconEmoji}🎉✨", fontSize = 44.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (lastWonReward.isJackpot) "جک‌پات طلایی برنده شدید!" else "تبریک! جایزه شما:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lastWonReward.labelFa,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Detailed won list
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (lastWonReward.coins > 0) {
                                Text(
                                    text = "+${lastWonReward.coins} سکه طلایی",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldDark
                                )
                            }
                            if (lastWonReward.gems > 0) {
                                Text(
                                    text = "+${lastWonReward.gems} الماس نیلگون",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0284C7)
                                )
                            }
                            if (lastWonReward.freeLetterHints > 0) {
                                Text(
                                    text = "+${lastWonReward.freeLetterHints} راهنمای حرف ذخیره‌شده 💡",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                            }
                            if (lastWonReward.freeWordHints > 0) {
                                Text(
                                    text = "+${lastWonReward.freeWordHints} فاش‌کردن کلمه کامل 🔮",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8B5CF6)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onDismissReward,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("claim_wheel_reward_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PersianTealPrimary)
                        ) {
                            Text(text = "دریافت جایزه و ادامه", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

private fun formatWheelTime(millis: Long): String {
    if (millis <= 0) return "آماده"
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
