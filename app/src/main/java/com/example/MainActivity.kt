package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ScreenRoute
import com.example.ui.components.BazaarPaymentDialog
import com.example.ui.components.DailyLoginBonusDialog
import com.example.ui.screens.BazaarStoreScreen
import com.example.ui.screens.CityMapScreen
import com.example.ui.screens.DailyChallengeScreen
import com.example.ui.screens.FortuneWheelScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProverbScreen
import com.example.ui.screens.SpeedLeagueScreen
import com.example.ui.screens.WordGameScreen
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                // Ensure natural RTL layout for Persian Persian language
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    QandParsiApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun QandParsiApp(viewModel: GameViewModel) {
    val route by viewModel.route.collectAsState()
    val coins by viewModel.coins.collectAsState()
    val gems by viewModel.gems.collectAsState()
    val bonusWordsCount by viewModel.bonusWordsCount.collectAsState()
    val isVip by viewModel.isVip.collectAsState()
    val isAdsRemoved by viewModel.isAdsRemoved.collectAsState()
    val isSoundEnabled by viewModel.soundEnabled.collectAsState()
    val isVibrationEnabled by viewModel.vibrationEnabled.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    // Word game state
    val currentLevel by viewModel.currentLevel.collectAsState()
    val currentLevelIndex by viewModel.currentLevelIndex.collectAsState()
    val shuffledLetters by viewModel.shuffledLetters.collectAsState()
    val selectedLetters by viewModel.selectedLetters.collectAsState()
    val selectedLetterIndices by viewModel.selectedLetterIndices.collectAsState()
    val foundWords by viewModel.foundWords.collectAsState()
    val revealedIndices by viewModel.revealedLetterIndices.collectAsState()
    val isLevelWon by viewModel.isLevelWon.collectAsState()

    // Proverb game state
    val currentProverb by viewModel.currentProverb.collectAsState()
    val currentProverbIndex by viewModel.currentProverbIndex.collectAsState()
    val proverbLetters by viewModel.proverbLetters.collectAsState()
    val selectedProverbIndices by viewModel.selectedProverbIndices.collectAsState()
    val proverbInput by viewModel.proverbInput.collectAsState()
    val isProverbWon by viewModel.isProverbWon.collectAsState()

    // Wheel state
    val wheelRotation by viewModel.wheelRotation.collectAsState()
    val isSpinning by viewModel.isSpinning.collectAsState()
    val lastWheelReward by viewModel.lastWheelReward.collectAsState()
    val isDailyFreeSpinAvailable by viewModel.isDailyFreeSpinAvailable.collectAsState()
    val timeUntilNextFreeSpinMillis by viewModel.timeUntilNextFreeSpinMillis.collectAsState()

    // Store & Ads state
    val activeBazaarProduct by viewModel.activeBazaarProduct.collectAsState()
    val bazaarPurchaseSuccess by viewModel.bazaarPurchaseSuccess.collectAsState()
    val isWatchingAd by viewModel.isWatchingRewardedAd.collectAsState()
    val adProgress by viewModel.adProgress.collectAsState()

    // Daily & Streak
    val dailyStreak by viewModel.dailyStreak.collectAsState()

    // 24-Hour Daily Login Bonus System
    val loginBonusDay by viewModel.loginBonusDay.collectAsState()
    val isLoginBonusAvailable by viewModel.isLoginBonusAvailable.collectAsState()
    val showDailyLoginDialog by viewModel.showDailyLoginDialog.collectAsState()
    val freeLetterHints by viewModel.freeLetterHints.collectAsState()
    val freeWordHints by viewModel.freeWordHints.collectAsState()
    val timeUntilNextBonusMillis by viewModel.timeUntilNextBonusMillis.collectAsState()

    // Hardware Back Handling
    BackHandler(enabled = route != ScreenRoute.HOME) {
        viewModel.handleBack()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_root_scaffold"),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF1E293B),
                    shadowElevation = 8.dp
                ) {
                    Text(
                        text = data.visuals.message,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = route,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screenTransition"
            ) { targetRoute ->
                when (targetRoute) {
                    ScreenRoute.HOME -> {
                        HomeScreen(
                            coins = coins,
                            gems = gems,
                            bonusWordsCount = bonusWordsCount,
                            currentLevelNumber = currentLevelIndex,
                            cityNameFa = currentLevel.cityNameFa,
                            dailyStreak = dailyStreak,
                            isDailyBonusAvailable = isLoginBonusAvailable,
                            timeUntilNextBonusMillis = timeUntilNextBonusMillis,
                            loginBonusDay = loginBonusDay,
                            freeLetterHints = freeLetterHints,
                            freeWordHints = freeWordHints,
                            isSoundEnabled = isSoundEnabled,
                            isVibrationEnabled = isVibrationEnabled,
                            onToggleSound = { viewModel.toggleSound() },
                            onToggleVibration = { viewModel.toggleVibration() },
                            onOpenDailyBonus = { viewModel.openDailyLoginDialog() },
                            onNavigate = { viewModel.navigateTo(it) },
                            onSmashPiggyBank = { viewModel.smashPiggyBank() }
                        )
                    }

                    ScreenRoute.WORD_GAME -> {
                        WordGameScreen(
                            level = currentLevel,
                            shuffledLetters = shuffledLetters,
                            selectedLetters = selectedLetters,
                            selectedLetterIndices = selectedLetterIndices,
                            foundWords = foundWords,
                            revealedIndices = revealedIndices,
                            isLevelWon = isLevelWon,
                            coins = coins,
                            gems = gems,
                            bonusWordsCount = bonusWordsCount,
                            freeLetterHints = freeLetterHints,
                            freeWordHints = freeWordHints,
                            isSoundEnabled = isSoundEnabled,
                            onToggleSound = { viewModel.toggleSound() },
                            onLetterTapped = { viewModel.onLetterTapped(it) },
                            onNodeSelected = { viewModel.selectWheelNodeIndex(it) },
                            onBacktrackNode = { viewModel.backtrackWheelNode() },
                            onClearSelected = { viewModel.clearSelectedLetters() },
                            onShuffle = { viewModel.shuffleLetters() },
                            onSubmitWord = { viewModel.submitCurrentWord() },
                            onHintLetter = { viewModel.useHintRevealLetter() },
                            onHintWord = { viewModel.useHintRevealWord() },
                            onNextLevel = { viewModel.goToNextLevel() },
                            onBack = { viewModel.handleBack() },
                            onOpenStore = { viewModel.navigateTo(ScreenRoute.BAZAAR_STORE) },
                            onSmashPiggyBank = { viewModel.smashPiggyBank() }
                        )
                    }

                    ScreenRoute.PROVERB_GAME -> {
                        ProverbScreen(
                            level = currentProverb,
                            currentLevelNumber = currentProverbIndex,
                            totalLevels = 10,
                            inputText = proverbInput,
                            scrambledLetters = proverbLetters,
                            selectedIndices = selectedProverbIndices,
                            isWon = isProverbWon,
                            coins = coins,
                            gems = gems,
                            bonusWordsCount = bonusWordsCount,
                            isSoundEnabled = isSoundEnabled,
                            onToggleSound = { viewModel.toggleSound() },
                            onLetterTapped = { idx, char -> viewModel.onProverbLetterTapped(idx, char) },
                            onRemoveLetterAt = { slotIdx -> viewModel.onRemoveProverbLetterAt(slotIdx) },
                            onBackspace = { viewModel.onProverbBackspace() },
                            onShuffle = { viewModel.shuffleProverbLetters() },
                            onSubmit = { viewModel.submitProverbAnswer() },
                            onNextLevel = { viewModel.nextProverbLevel() },
                            onBack = { viewModel.handleBack() },
                            onOpenStore = { viewModel.navigateTo(ScreenRoute.BAZAAR_STORE) }
                        )
                    }

                    ScreenRoute.DAILY_CHALLENGE -> {
                        DailyChallengeScreen(
                            challenge = viewModel.dailyChallenge,
                            dailyStreak = dailyStreak,
                            coins = coins,
                            gems = gems,
                            bonusWordsCount = bonusWordsCount,
                            isSoundEnabled = isSoundEnabled,
                            onToggleSound = { viewModel.toggleSound() },
                            onBack = { viewModel.handleBack() },
                            onOpenStore = { viewModel.navigateTo(ScreenRoute.BAZAAR_STORE) },
                            onRewardEarned = { _, _ -> }
                        )
                    }

                    ScreenRoute.SPEED_LEAGUE -> {
                        SpeedLeagueScreen(
                            users = viewModel.leaderboardUsers,
                            coins = coins,
                            gems = gems,
                            bonusWordsCount = bonusWordsCount,
                            isSoundEnabled = isSoundEnabled,
                            onToggleSound = { viewModel.toggleSound() },
                            onBack = { viewModel.handleBack() },
                            onOpenStore = { viewModel.navigateTo(ScreenRoute.BAZAAR_STORE) }
                        )
                    }

                    ScreenRoute.FORTUNE_WHEEL -> {
                        FortuneWheelScreen(
                            coins = coins,
                            gems = gems,
                            bonusWordsCount = bonusWordsCount,
                            freeLetterHints = freeLetterHints,
                            freeWordHints = freeWordHints,
                            isDailyFreeSpinAvailable = isDailyFreeSpinAvailable,
                            timeUntilNextFreeSpinMillis = timeUntilNextFreeSpinMillis,
                            isSoundEnabled = isSoundEnabled,
                            isSpinning = isSpinning,
                            wheelRotation = wheelRotation,
                            lastWonReward = lastWheelReward,
                            onToggleSound = { viewModel.toggleSound() },
                            onSpinWheel = { viewModel.spinFortuneWheel() },
                            onWatchAdForFreeSpin = { viewModel.watchAdForFortuneSpin() },
                            onDismissReward = { viewModel.dismissWheelReward() },
                            onBack = { viewModel.handleBack() },
                            onOpenStore = { viewModel.navigateTo(ScreenRoute.BAZAAR_STORE) }
                        )
                    }

                    ScreenRoute.BAZAAR_STORE -> {
                        BazaarStoreScreen(
                            coins = coins,
                            gems = gems,
                            bonusWordsCount = bonusWordsCount,
                            isSoundEnabled = isSoundEnabled,
                            isWatchingAd = isWatchingAd,
                            adProgress = adProgress,
                            isVip = isVip,
                            isAdsRemoved = isAdsRemoved,
                            onToggleSound = { viewModel.toggleSound() },
                            onSelectProduct = { viewModel.openBazaarPayment(it) },
                            onWatchAd = { viewModel.watchRewardedVideoAd() },
                            onBack = { viewModel.handleBack() }
                        )
                    }

                    ScreenRoute.CITY_MAP -> {
                        CityMapScreen(
                            currentUnlockedLevel = currentLevelIndex,
                            coins = coins,
                            gems = gems,
                            bonusWordsCount = bonusWordsCount,
                            isSoundEnabled = isSoundEnabled,
                            onToggleSound = { viewModel.toggleSound() },
                            onSelectLevel = { viewModel.selectLevelFromMap(it) },
                            onBack = { viewModel.handleBack() },
                            onOpenStore = { viewModel.navigateTo(ScreenRoute.BAZAAR_STORE) }
                        )
                    }
                }
            }

            // 24-Hour Daily Login Bonus Dialog
            if (showDailyLoginDialog) {
                DailyLoginBonusDialog(
                    currentStreakDay = loginBonusDay,
                    isAvailable = isLoginBonusAvailable,
                    timeUntilNextMillis = timeUntilNextBonusMillis,
                    onClaim = { doubleWithAd -> viewModel.claimDailyLoginBonus(doubleWithAd) },
                    onDismiss = { viewModel.dismissDailyLoginDialog() }
                )
            }

            // Cafe Bazaar Payment Dialog Overlay
            activeBazaarProduct?.let { product ->
                BazaarPaymentDialog(
                    product = product,
                    onConfirm = { viewModel.confirmBazaarPurchase(it) },
                    onDismiss = { viewModel.dismissBazaarDialog() }
                )
            }

            // Purchase Success Dialog
            bazaarPurchaseSuccess?.let { product ->
                Dialog(onDismissRequest = { viewModel.dismissPurchaseSuccess() }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(14.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "✅🛍️✨", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "خرید از کافه بازار با موفقیت انجام شد!",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00A651),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "بسته «${product.titleFa}» به حساب شما اضافه شد و آماده استفاده است.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Button(
                                onClick = { viewModel.dismissPurchaseSuccess() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A651))
                            ) {
                                Text(text = "عالیه! بازگشت به بازی", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
