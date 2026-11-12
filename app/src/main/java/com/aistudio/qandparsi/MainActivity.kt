package com.aistudio.qandparsi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aistudio.qandparsi.data.model.ScreenRoute
import com.aistudio.qandparsi.ads.AdManager
import com.aistudio.qandparsi.ads.TapsellBannerAd
import com.aistudio.qandparsi.billing.BazaarBillingManager
import com.aistudio.qandparsi.ui.components.BazaarPaymentDialog
import com.aistudio.qandparsi.ui.components.DailyLoginBonusDialog
import com.aistudio.qandparsi.ui.screens.BazaarStoreScreen
import com.aistudio.qandparsi.ui.screens.CityMapScreen
import com.aistudio.qandparsi.ui.screens.DailyChallengeScreen
import com.aistudio.qandparsi.ui.screens.FortuneWheelScreen
import com.aistudio.qandparsi.ui.screens.HomeScreen
import com.aistudio.qandparsi.ui.screens.ProverbScreen
import com.aistudio.qandparsi.ui.screens.SpeedLeagueScreen
import com.aistudio.qandparsi.ui.screens.WordGameScreen
import com.aistudio.qandparsi.ui.theme.GoldSecondary
import com.aistudio.qandparsi.ui.theme.MyApplicationTheme
import com.aistudio.qandparsi.viewmodel.GameViewModel
import ir.cafebazaar.poolakey.entity.PurchaseInfo

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AdManager.initialize(this)

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
    val isWatchingAd by viewModel.isWatchingRewardedAd.collectAsState()
    val pendingRewardedAdPurpose by viewModel.pendingRewardedAdPurpose.collectAsState()
    val pendingBazaarPurchase by viewModel.pendingBazaarPurchase.collectAsState()

    // Daily challenge state
    val dailyFoundWords by viewModel.dailyFoundWords.collectAsState()
    val dailySelectedIndices by viewModel.dailySelectedIndices.collectAsState()
    val isDailyWon by viewModel.isDailyWon.collectAsState()

    // Daily & Streak
    val dailyStreak by viewModel.dailyStreak.collectAsState()

    // Leaderboard
    val leaderboardUsers by viewModel.leaderboardUsers.collectAsState()

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

    // Show the real Tapsell rewarded video. Rewards are credited by the ViewModel only when
    // the SDK fires onRewarded(); closing or failing the ad grants nothing.
    val activity = LocalActivity.current
    LaunchedEffect(pendingRewardedAdPurpose, activity) {
        if (pendingRewardedAdPurpose == null) return@LaunchedEffect
        val host = activity
        if (host == null) {
            viewModel.onRewardedAdFailed("Activity unavailable")
            return@LaunchedEffect
        }
        AdManager.showRewarded(
            activity = host,
            listener = object : AdManager.RewardedAdListener {
                override fun onLoading() = Unit
                override fun onShown() = viewModel.onRewardedAdShown()
                override fun onRewarded() = viewModel.onRewardedAdRewarded()
                override fun onClosed() = viewModel.onRewardedAdClosed()
                override fun onFailed(message: String) = viewModel.onRewardedAdFailed(message)
            }
        )
    }

    // Cafe Bazaar purchase flow. The billing manager is created once per Activity (as a
    // ComponentActivity) and connected for the Activity's lifetime.
    val billingManager = remember(activity) {
        (activity as? ComponentActivity)?.let { act ->
            BazaarBillingManager(act, BuildConfig.BAZAAR_RSA_PUBLIC_KEY)
        }
    }
    DisposableEffect(billingManager) {
        billingManager?.connect()
        onDispose { billingManager?.disconnect() }
    }

    LaunchedEffect(pendingBazaarPurchase, billingManager) {
        val product = pendingBazaarPurchase ?: return@LaunchedEffect
        val mgr = billingManager
        if (mgr == null) {
            viewModel.onBazaarPurchaseFailed("پرداخت در دسترس نیست")
            return@LaunchedEffect
        }
        mgr.purchase(
            sku = product.sku,
            isSubscription = product.isVip,
            listener = object : BazaarBillingManager.PurchaseResultListener {
                override fun onPurchaseSucceed(info: PurchaseInfo) = viewModel.onBazaarPurchaseSucceed(info)
                override fun onPurchaseCanceled() = viewModel.onBazaarPurchaseCanceled()
                override fun onPurchaseFailed(message: String) = viewModel.onBazaarPurchaseFailed(message)
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_root_scaffold"),
        bottomBar = {
            // Keep the banner out of active gameplay and respect the "remove ads" entitlement.
            val gameplayRoutes = setOf(
                ScreenRoute.WORD_GAME,
                ScreenRoute.PROVERB_GAME,
                ScreenRoute.DAILY_CHALLENGE,
                ScreenRoute.FORTUNE_WHEEL
            )
            if (!isAdsRemoved && route !in gameplayRoutes) {
                TapsellBannerAd(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        },
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
                            totalLevels = viewModel.totalProverbLevels,
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
                            selectedIndices = dailySelectedIndices,
                            foundWords = dailyFoundWords,
                            isWon = isDailyWon,
                            onToggleSound = { viewModel.toggleSound() },
                            onSelectLetter = { viewModel.selectDailyLetter(it) },
                            onClearSelection = { viewModel.clearDailySelection() },
                            onSubmit = { viewModel.submitDailyWord() },
                            onBack = { viewModel.handleBack() },
                            onOpenStore = { viewModel.navigateTo(ScreenRoute.BAZAAR_STORE) }
                        )
                    }

                    ScreenRoute.SPEED_LEAGUE -> {
                        SpeedLeagueScreen(
                            users = leaderboardUsers,
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

            // Cafe Bazaar product information dialog. It never completes a purchase: real
            // billing needs a verified Bazaar IAB (Poolakey) setup that is not configured here.
            activeBazaarProduct?.let { product ->
                BazaarPaymentDialog(
                    product = product,
                    onConfirmPurchase = { viewModel.confirmBazaarPurchase() },
                    onDismiss = { viewModel.dismissBazaarDialog() }
                )
            }
        }
    }
}
