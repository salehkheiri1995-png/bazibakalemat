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
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
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
import androidx.compose.ui.window.Dialog
import com.aistudio.qandparsi.data.model.ProverbLevel
import com.aistudio.qandparsi.ui.components.TopBarHeader
import com.aistudio.qandparsi.ui.theme.CoinYellow
import com.aistudio.qandparsi.ui.theme.GoldDark
import com.aistudio.qandparsi.ui.theme.GoldSecondary
import com.aistudio.qandparsi.ui.theme.PersianTealPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProverbScreen(
    level: ProverbLevel,
    currentLevelNumber: Int,
    totalLevels: Int,
    inputText: String,
    scrambledLetters: List<Char>,
    selectedIndices: List<Int>,
    isWon: Boolean,
    coins: Int,
    gems: Int,
    bonusWordsCount: Int,
    isSoundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onLetterTapped: (Int, Char) -> Unit,
    onRemoveLetterAt: (Int) -> Unit,
    onBackspace: () -> Unit,
    onShuffle: () -> Unit,
    onSubmit: () -> Unit,
    onNextLevel: () -> Unit,
    onBack: () -> Unit,
    onOpenStore: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                // Progress Bar Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مرحله $currentLevelNumber از $totalLevels",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldSecondary
                        )
                        Text(
                            text = "حدس ضرب‌المثل شیرین فارسی",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Emoji Clue Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = level.emojiClue,
                            fontSize = 38.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "نشانه‌های تصویری معما",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Masked Proverb Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldSecondary.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = level.maskedProverb,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "(کلمه ${level.missingWord.length} حرفی)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Input Word Tile Slots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val targetLen = level.missingWord.length
                    for (i in 0 until targetLen) {
                        val char = if (i < inputText.length) inputText[i] else null
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (char != null) Brush.verticalGradient(
                                        listOf(Color(0xFF00897B), Color(0xFF004D40))
                                    ) else Brush.verticalGradient(
                                        listOf(Color(0xFF334155), Color(0xFF1E293B))
                                    )
                                )
                                .border(1.5.dp, if (char != null) GoldSecondary else Color(0xFF475569), RoundedCornerShape(12.dp))
                                .clickable(enabled = char != null) { onRemoveLetterAt(i) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char?.toString() ?: "",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Backspace button
                    IconButton(
                        onClick = onBackspace,
                        enabled = inputText.isNotEmpty(),
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("proverb_backspace_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "حذف حرف",
                            tint = if (inputText.isNotEmpty()) Color(0xFFEF4444) else Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Toolbar with Shuffle & Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = onShuffle,
                        shape = RoundedCornerShape(10.dp),
                        color = GoldSecondary.copy(alpha = 0.14f),
                        modifier = Modifier.testTag("shuffle_proverb_letters_btn")
                    ) {
                        Text(
                            text = "بُر زدن 🔀",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Text(
                        text = "حروف را برای ساخت واژه انتخاب کنید:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Letter Choices Keyboard
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 5
                ) {
                    scrambledLetters.forEachIndexed { idx, char ->
                        val isSelected = selectedIndices.contains(idx)

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected)
                                        Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                                    else
                                        Brush.verticalGradient(listOf(Color(0xFFFDE68A), Color(0xFFF59E0B)))
                                )
                                .border(
                                    width = 1.5.dp,
                                    color = if (isSelected) Color(0xFF475569) else Color(0xFFD97706),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = !isSelected) { onLetterTapped(idx, char) }
                                .testTag("proverb_letter_$idx"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) Color(0xFF64748B) else Color(0xFF0F172A)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Submit Button
                Button(
                    onClick = onSubmit,
                    enabled = inputText.length == level.missingWord.length,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_proverb_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = PersianTealPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "بررسی پاسخ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }

        // Proverb Solved Celebration Dialog
        if (isWon) {
            Dialog(onDismissRequest = {}) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .testTag("proverb_won_dialog"),
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
                        Text(text = "✨📜✨", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "احسنت! ضرب‌المثل را درست گفتی",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "«${level.proverbFull}»",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Meaning Box
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "مفهوم و حکمت ضرب‌المثل:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = level.meaning,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CoinYellow.copy(alpha = 0.15f))
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = CoinYellow)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+${level.rewardCoins} سکه طلایی",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onNextLevel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("proverb_next_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PersianTealPrimary)
                        ) {
                            Text(text = "ضرب‌المثل بعدی", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                            Text(text = "بازگشت به منو")
                        }
                    }
                }
            }
        }
    }
}
