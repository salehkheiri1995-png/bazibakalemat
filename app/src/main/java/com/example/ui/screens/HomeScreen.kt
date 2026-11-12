package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ScreenRoute
import com.example.ui.components.TopBarHeader
import com.example.ui.theme.CoinYellow
import com.example.ui.theme.CrimsonTertiary
import com.example.ui.theme.GemBlue
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.PersianTealDark
import com.example.ui.theme.PersianTealLight
import com.example.ui.theme.PersianTealPrimary

@Composable
fun HomeScreen(
    coins: Int,
    gems: Int,
    bonusWordsCount: Int,
    currentLevelNumber: Int,
    cityNameFa: String,
    dailyStreak: Int,
    isDailyBonusAvailable: Boolean,
    timeUntilNextBonusMillis: Long,
    loginBonusDay: Int,
    freeLetterHints: Int,
    freeWordHints: Int,
    isSoundEnabled: Boolean,
    isVibrationEnabled: Boolean,
    onToggleSound: () -> Unit,
    onToggleVibration: () -> Unit,
    onOpenDailyBonus: () -> Unit,
    onNavigate: (ScreenRoute) -> Unit,
    onSmashPiggyBank: () -> Unit
) {
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Wallet Header
        TopBarHeader(
            coins = coins,
            gems = gems,
            bonusWordsCount = bonusWordsCount,
            isSoundEnabled = isSoundEnabled,
            onToggleSound = onToggleSound,
            onOpenStore = { onNavigate(ScreenRoute.BAZAAR_STORE) },
            onPiggyBankClick = onSmashPiggyBank
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Banner Card with Palace Artwork
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_hero_banner),
                    contentDescription = "قند پارسی",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Shaded gradient overlay for crisp text
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x99000000),
                                    Color(0xEE0A192F)
                                )
                            )
                        )
                )

                // Overlay Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "قند پارسی",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "بازی کلمات و ضرب‌المثل‌های اصیل ایرانی",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFDE68A),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Streak & Status Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onToggleVibration, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Default.Vibration,
                                contentDescription = "لرزش",
                                tint = if (isVibrationEnabled) GoldDark else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "استریک روزانه: $dailyStreak روز",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "استریک",
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 24-Hour Daily Login Bonus Banner
            DailyLoginBonusBanner(
                isAvailable = isDailyBonusAvailable,
                streakDay = loginBonusDay,
                timeUntilNextMillis = timeUntilNextBonusMillis,
                freeLetterHints = freeLetterHints,
                freeWordHints = freeWordHints,
                onClick = onOpenDailyBonus
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Main Primary Play Button (Resume Stage)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .scale(pulseScale)
            ) {
                Button(
                    onClick = { onNavigate(ScreenRoute.WORD_GAME) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .testTag("play_word_game_button"),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF00796B),
                                        Color(0xFF009688),
                                        Color(0xFF004D40)
                                    )
                                )
                            )
                            .border(2.dp, GoldSecondary, RoundedCornerShape(20.dp))
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(GoldSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "شروع",
                                    tint = Color.Black,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "شروع مرحله $currentLevelNumber",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = cityNameFa,
                                    fontSize = 13.sp,
                                    color = Color(0xFFFDE68A)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Grid of Game Modes & Features
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Row 1: Proverb Mode & Daily Challenge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Proverb Guessing
                    HomeFeatureCard(
                        title = "حدس ضرب‌المثل",
                        subtitle = "حکمت‌های کهن",
                        icon = "📜",
                        color = Color(0xFF8B5CF6),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_proverbs"),
                        onClick = { onNavigate(ScreenRoute.PROVERB_GAME) }
                    )

                    // Daily Challenge
                    HomeFeatureCard(
                        title = "چالش روزانه",
                        subtitle = "جایزه دوبل",
                        icon = "⚡",
                        color = Color(0xFFF59E0B),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_daily"),
                        onClick = { onNavigate(ScreenRoute.DAILY_CHALLENGE) }
                    )
                }

                // Row 2: Fortune Wheel & Word League
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fortune Wheel
                    HomeFeatureCard(
                        title = "گردونه شانس",
                        subtitle = "چرخش روزانه",
                        icon = "🎡",
                        color = Color(0xFFEC4899),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_wheel"),
                        onClick = { onNavigate(ScreenRoute.FORTUNE_WHEEL) }
                    )

                    // Speed League
                    HomeFeatureCard(
                        title = "لیگ دانشمندان",
                        subtitle = "رقابت آنلاین",
                        icon = "🏆",
                        color = Color(0xFF3B82F6),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_league"),
                        onClick = { onNavigate(ScreenRoute.SPEED_LEAGUE) }
                    )
                }

                // Row 3: Iranian Cities Map & Bazaar In-App Store
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // City Map
                    HomeFeatureCard(
                        title = "نقشه شهرها",
                        subtitle = "۷ استان ایران",
                        icon = "🗺️",
                        color = Color(0xFF10B981),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_map"),
                        onClick = { onNavigate(ScreenRoute.CITY_MAP) }
                    )

                    // Cafe Bazaar Store
                    HomeFeatureCard(
                        title = "فروشگاه بازار",
                        subtitle = "سکه و الماس",
                        icon = "🛍️",
                        color = Color(0xFF00A651),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_store"),
                        onClick = { onNavigate(ScreenRoute.BAZAAR_STORE) }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeFeatureCard(
    title: String,
    subtitle: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 24.sp)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                }
            }
        }
    }
}

@Composable
fun DailyLoginBonusBanner(
    isAvailable: Boolean,
    streakDay: Int,
    timeUntilNextMillis: Long,
    freeLetterHints: Int,
    freeWordHints: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() }
            .testTag("daily_bonus_banner"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAvailable) Color(0xFF004D40) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isAvailable) 4.dp else 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (isAvailable) 2.dp else 1.dp,
                    color = if (isAvailable) GoldSecondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left CTA Button / Badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isAvailable) GoldSecondary else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = if (isAvailable) "دریافت هدیه 🎁" else "مشاهده تقویم 📅",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAvailable) Color.Black else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                // Right Info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "پاداش ورود روزانه (روز $streakDay از ۷)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isAvailable) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (isAvailable) {
                            Text(
                                text = "هدیه امروز ۲۴ ساعته شما آماده دریافت است!",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = CoinYellow
                            )
                        } else {
                            val hours = (timeUntilNextMillis / 1000) / 3600
                            val minutes = ((timeUntilNextMillis / 1000) % 3600) / 60
                            val timeStr = String.format("%02d:%02d", hours, minutes)
                            Text(
                                text = "دریافت شده ✓ | پاداش بعدی تا $timeStr ساعت دیگر",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }

                        if (freeLetterHints > 0 || freeWordHints > 0) {
                            Text(
                                text = "راهنماهای فعال: $freeLetterHints حرف 💡 | $freeWordHints کلمه 🔮",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAvailable) CoinYellow else Color(0xFF10B981)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (isAvailable) GoldSecondary.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if (isAvailable) "🎁" else "🗓️", fontSize = 22.sp)
                    }
                }
            }
        }
    }
}

