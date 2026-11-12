package com.aistudio.qandparsi.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.aistudio.qandparsi.data.model.GameLevel
import com.aistudio.qandparsi.ui.components.TopBarHeader
import com.aistudio.qandparsi.ui.theme.CoinYellow
import com.aistudio.qandparsi.ui.theme.GoldDark
import com.aistudio.qandparsi.ui.theme.GoldSecondary
import com.aistudio.qandparsi.ui.theme.PersianTealDark
import com.aistudio.qandparsi.ui.theme.PersianTealPrimary
import com.aistudio.qandparsi.util.WheelGeometry
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordGameScreen(
    level: GameLevel,
    shuffledLetters: List<Char>,
    selectedLetters: List<Char>,
    selectedLetterIndices: List<Int>,
    foundWords: Set<String>,
    revealedIndices: Map<String, Set<Int>>,
    isLevelWon: Boolean,
    coins: Int,
    gems: Int,
    bonusWordsCount: Int,
    freeLetterHints: Int = 0,
    freeWordHints: Int = 0,
    isSoundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onLetterTapped: (Char) -> Unit,
    onNodeSelected: (Int) -> Unit,
    onBacktrackNode: () -> Unit,
    onClearSelected: () -> Unit,
    onShuffle: () -> Unit,
    onSubmitWord: () -> Unit,
    onHintLetter: () -> Unit,
    onHintWord: () -> Unit,
    onNextLevel: () -> Unit,
    onBack: () -> Unit,
    onOpenStore: () -> Unit,
    onSmashPiggyBank: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Bar
            TopBarHeader(
                coins = coins,
                gems = gems,
                bonusWordsCount = bonusWordsCount,
                isSoundEnabled = isSoundEnabled,
                onToggleSound = onToggleSound,
                onOpenStore = onOpenStore,
                onBack = onBack,
                onPiggyBankClick = onSmashPiggyBank
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Stage & City title banner
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "یافته شده: ${foundWords.size} از ${level.targetWords.size}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "مرحله ${level.stageNumber}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldSecondary
                            )
                            Text(
                                text = level.cityNameFa,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Crossword Words Target Grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    level.targetWords.forEach { word ->
                        val isFound = foundWords.contains(word)
                        val revealed = revealedIndices[word] ?: emptySet()

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            word.forEachIndexed { idx, char ->
                                val showLetter = isFound || revealed.contains(idx)
                                WordTile(
                                    char = if (showLetter) char else null,
                                    isFound = isFound
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Selected Word Indicator Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, GoldSecondary.copy(alpha = 0.6f), RoundedCornerShape(26.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Clear Button
                        IconButton(
                            onClick = onClearSelected,
                            enabled = selectedLetters.isNotEmpty(),
                            modifier = Modifier
                                .testTag("clear_letters_btn")
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "پاک کردن",
                                tint = if (selectedLetters.isNotEmpty()) Color(0xFFEF4444) else Color.Transparent
                            )
                        }

                        // Formed Word Text
                        Text(
                            text = if (selectedLetters.isEmpty()) "حروف را با کشیدن انگشت وصل کنید..." else selectedLetters.joinToString(""),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (selectedLetters.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else GoldSecondary,
                            letterSpacing = 2.sp
                        )

                        // Submit Button
                        IconButton(
                            onClick = onSubmitWord,
                            enabled = selectedLetters.isNotEmpty(),
                            modifier = Modifier
                                .testTag("submit_word_btn")
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (selectedLetters.isNotEmpty()) Color(0xFF10B981) else Color.Transparent)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "تایید کلمه",
                                tint = if (selectedLetters.isNotEmpty()) Color.White else Color.Transparent
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Circular Letter Wheel with Real-time Path-Line and Drag Animation
                LetterWheelWithDrag(
                    shuffledLetters = shuffledLetters,
                    selectedIndices = selectedLetterIndices,
                    onNodeSelected = onNodeSelected,
                    onBacktrackNode = onBacktrackNode,
                    onClearSelected = onClearSelected,
                    onShuffle = onShuffle,
                    onSubmitWord = onSubmitWord
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Letter Bank Chips for alternate touch / accessibility
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    shuffledLetters.forEachIndexed { idx, char ->
                        val isUsed = selectedLetterIndices.contains(idx)

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isUsed) Color(0xFF334155)
                                    else Color(0xFF00796B)
                                )
                                .border(
                                    width = 1.5.dp,
                                    color = if (isUsed) Color(0xFF475569) else GoldSecondary,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    if (isUsed) {
                                        if (selectedLetterIndices.lastOrNull() == idx) {
                                            onBacktrackNode()
                                        }
                                    } else {
                                        onNodeSelected(idx)
                                    }
                                }
                                .testTag("letter_chip_$idx"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isUsed) Color(0xFF94A3B8) else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Hint Controls Toolbar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Hint 1: Reveal Letter
                    HintButton(
                        icon = Icons.Default.Search,
                        title = "فاش کردن حرف",
                        costText = if (freeLetterHints > 0) "$freeLetterHints رایگان! 💡" else "۵۰ سکه",
                        modifier = Modifier
                            .weight(1f)
                            .testTag("hint_letter_btn"),
                        onClick = onHintLetter
                    )

                    // Hint 2: Reveal Full Word
                    HintButton(
                        icon = Icons.Default.Lightbulb,
                        title = "فاش کردن کلمه",
                        costText = if (freeWordHints > 0) "$freeWordHints رایگان! 🔮" else "۱۲۰ سکه",
                        modifier = Modifier
                            .weight(1f)
                            .testTag("hint_word_btn"),
                        onClick = onHintWord
                    )
                }
            }
        }

        // Level Completed Victory Dialog
        if (isLevelWon) {
            Dialog(onDismissRequest = {}) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .testTag("victory_dialog"),
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
                        Text(text = "🎉", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "آفرین! مرحله با موفقیت فتح شد",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "تمام کلمات «${level.cityNameFa}» را با مهارت پیدا کردی!",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Star ratings
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(3) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldSecondary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Coins won
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CoinYellow.copy(alpha = 0.15f))
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = CoinYellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+${level.rewardCoins} سکه جایزه",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onNextLevel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("next_level_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PersianTealPrimary)
                        ) {
                            Text(text = "مرحله بعدی", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(text = "بازگشت به خانه")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WordTile(
    char: Char?,
    isFound: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isFound) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tileScale"
    )

    Box(
        modifier = Modifier
            .size(42.dp)
            .scale(scale)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isFound) Brush.verticalGradient(
                    listOf(
                        Color(0xFF00897B),
                        Color(0xFF004D40)
                    )
                )
                else Brush.verticalGradient(
                    listOf(
                        Color(0xFF334155),
                        Color(0xFF1E293B)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                color = if (isFound) GoldSecondary else Color(0xFF475569),
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (char != null) {
            Text(
                text = char.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isFound) Color.White else GoldSecondary
            )
        } else {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF64748B))
            )
        }
    }
}

@Composable
fun HintButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    costText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(56.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(GoldDark.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = GoldDark,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = costText, fontSize = 10.sp, color = GoldSecondary)
            }
        }
    }
}

@Composable
private fun LetterWheelWithDrag(
    shuffledLetters: List<Char>,
    selectedIndices: List<Int>,
    onNodeSelected: (Int) -> Unit,
    onBacktrackNode: () -> Unit,
    onClearSelected: () -> Unit,
    onShuffle: () -> Unit,
    onSubmitWord: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val wheelSize = 270.dp
    val radius = 88.dp
    val wheelSizePx = with(density) { wheelSize.toPx() }
    val radiusPx = with(density) { radius.toPx() }
    val centerPx = Offset(wheelSizePx / 2f, wheelSizePx / 2f)
    val hitRadiusPx = with(density) { 42.dp.toPx() }
    val centerIgnoreRadiusPx = with(density) { 34.dp.toPx() }

    val count = shuffledLetters.size
    val nodeCenters = remember(count, wheelSizePx, radiusPx) {
        WheelGeometry.nodeCenters(count, centerPx, radiusPx)
    }

    var currentFingerPos by remember { mutableStateOf<Offset?>(null) }
    var isDragging by remember { mutableStateOf(false) }

    // Instant local state for the active drag path to eliminate multi-thread/recomposition lag
    val activePathIndices = remember { mutableStateListOf<Int>() }

    // Keep updated callbacks across recompositions without cancelling active pointerInput jobs
    val currentOnNodeSelected by rememberUpdatedState(onNodeSelected)
    val currentOnBacktrackNode by rememberUpdatedState(onBacktrackNode)
    val currentOnClearSelected by rememberUpdatedState(onClearSelected)
    val currentOnSubmitWord by rememberUpdatedState(onSubmitWord)

    // Pulsing animation for real-time finger feedback effect
    val infiniteTransition = rememberInfiniteTransition(label = "finger_pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = with(density) { 14.dp.toPx() },
        targetValue = with(density) { 28.dp.toPx() },
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    // While dragging, use activePathIndices for instant 120Hz continuous visual feedback.
    // When finger is up, display selectedIndices (e.g. from bottom chip taps).
    val displayedIndices = if (isDragging) {
        activePathIndices.filter { it in nodeCenters.indices }
    } else {
        selectedIndices.filter { it in nodeCenters.indices }
    }

    Box(
        modifier = modifier
            .size(wheelSize)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF004D40),
                        Color(0xFF00241E)
                    ),
                    center = centerPx,
                    radius = wheelSizePx / 2f
                )
            )
            .border(3.dp, GoldSecondary.copy(alpha = 0.85f), CircleShape)
            .pointerInput(count) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val downPos = down.position

                    // Ignore touch inside center shuffle button
                    if (WheelGeometry.distance(downPos, centerPx) < centerIgnoreRadiusPx) {
                        return@awaitEachGesture
                    }

                    // Start fresh continuous drag session
                    activePathIndices.clear()
                    currentOnClearSelected()
                    isDragging = true
                    currentFingerPos = downPos

                    // Check if initial touch landed on a letter node
                    val initialHit = WheelGeometry.nearestIndex(downPos, nodeCenters, hitRadiusPx)
                    if (initialHit != null) {
                        activePathIndices.add(initialHit)
                        currentOnNodeSelected(initialHit)
                    }
                    down.consume()

                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull() ?: break
                        if (!change.pressed) {
                            change.consume()
                            break
                        }

                        val currentPos = change.position
                        currentFingerPos = currentPos
                        change.consume()

                        val nearIndex = WheelGeometry.nearestIndex(currentPos, nodeCenters, hitRadiusPx)

                        if (nearIndex != null) {
                            when {
                                activePathIndices.isEmpty() -> {
                                    // First letter encountered during slide
                                    activePathIndices.add(nearIndex)
                                    currentOnNodeSelected(nearIndex)
                                }
                                activePathIndices.lastOrNull() == nearIndex -> {
                                    // Finger is still on the current active node
                                }
                                activePathIndices.size >= 2 && activePathIndices[activePathIndices.size - 2] == nearIndex -> {
                                    // Smooth backtrack to previous node
                                    activePathIndices.removeAt(activePathIndices.lastIndex)
                                    currentOnBacktrackNode()
                                }
                                !activePathIndices.contains(nearIndex) -> {
                                    // Latch onto new letter in chain
                                    activePathIndices.add(nearIndex)
                                    currentOnNodeSelected(nearIndex)
                                }
                            }
                        }
                    }

                    // Finger released
                    val completedWordSize = activePathIndices.size
                    isDragging = false
                    currentFingerPos = null

                    if (completedWordSize >= 2) {
                        currentOnSubmitWord()
                    } else {
                        currentOnClearSelected()
                    }
                    activePathIndices.clear()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Visual Path-Line Canvas Effect
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (displayedIndices.isNotEmpty()) {
                val path = Path().apply {
                    val start = nodeCenters[displayedIndices[0]]
                    moveTo(start.x, start.y)
                    for (i in 1 until displayedIndices.size) {
                        val pt = nodeCenters[displayedIndices[i]]
                        lineTo(pt.x, pt.y)
                    }
                    if (isDragging && currentFingerPos != null) {
                        val finger = currentFingerPos!!
                        lineTo(finger.x, finger.y)
                    }
                }

                // Layer 1: Ambient Golden Halo / Glow (wide soft stroke)
                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0x66FFD54F),
                            Color(0x88FFA000),
                            Color(0x55FF6F00)
                        )
                    ),
                    style = Stroke(
                        width = 18.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Layer 2: Neon Core Beam
                drawPath(
                    path = path,
                    color = Color(0xFFFFCA28),
                    style = Stroke(
                        width = 9.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Layer 3: Brilliant Light Laser Center
                drawPath(
                    path = path,
                    color = Color(0xFFFFFDE7),
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Sparkling center dots at each connected letter node
                displayedIndices.forEach { idx ->
                    val center = nodeCenters[idx]
                    drawCircle(
                        color = Color(0xFFFFF7C2),
                        radius = 6.dp.toPx(),
                        center = center
                    )
                }
            }

            // Real-time finger follower pulse and particle orb
            if (isDragging && currentFingerPos != null) {
                val finger = currentFingerPos!!
                // Expanding dynamic pulse wave
                drawCircle(
                    color = Color(0xFFFFD54F).copy(alpha = pulseAlpha),
                    radius = pulseRadius,
                    center = finger,
                    style = Stroke(width = 2.5.dp.toPx())
                )
                // High-energy particle orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFFFB300),
                            Color(0x00FF8F00)
                        ),
                        center = finger,
                        radius = 12.dp.toPx()
                    ),
                    radius = 10.dp.toPx(),
                    center = finger
                )
            }
        }

        // Circular Letter Nodes
        shuffledLetters.forEachIndexed { i, char ->
            val isSelected = displayedIndices.contains(i)
            val sequenceOrder = if (isSelected) displayedIndices.indexOf(i) + 1 else null

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.20f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "node_scale_$i"
            )

            val angle = (2 * Math.PI * i / count) - (Math.PI / 2)
            val xOffset = (cos(angle) * radius.value).dp
            val yOffset = (sin(angle) * radius.value).dp

            Box(
                modifier = Modifier
                    // absoluteOffset keeps the physical position identical in RTL layouts;
                    // the direction-aware `offset` mirrors X and desyncs nodes from the
                    // Canvas drawing and pointer hit-testing (drag-left selected right).
                    .absoluteOffset(x = xOffset, y = yOffset)
                    .scale(scale)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected)
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFFE082),
                                    Color(0xFFFFB300),
                                    Color(0xFFF57C00)
                                )
                            )
                        else
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFFFDE7),
                                    Color(0xFFFFECB3)
                                )
                            )
                    )
                    .border(
                        width = if (isSelected) 3.dp else 2.dp,
                        color = if (isSelected) Color(0xFFFFF7C2) else Color(0xFFD97706),
                        shape = CircleShape
                    )
                    .testTag("letter_node_$i"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char.toString(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSelected) Color(0xFF451A03) else Color(0xFF1E293B)
                )

                // Sequence badge (1, 2, 3...)
                if (sequenceOrder != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 2.dp, end = 2.dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFB45309)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = sequenceOrder.toString(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Center Shuffle Button
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, GoldSecondary, CircleShape)
                .clickable { onShuffle() }
                .testTag("shuffle_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "بر زدن",
                    tint = GoldSecondary,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "بُر",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldSecondary
                )
            }
        }
    }
}
