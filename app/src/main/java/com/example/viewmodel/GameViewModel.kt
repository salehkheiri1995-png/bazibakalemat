package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.GamePreferences
import com.example.data.model.DailyChallenge
import com.example.data.model.GameLevel
import com.example.data.model.LeaderboardUser
import com.example.data.model.ProverbLevel
import com.example.data.model.ScreenRoute
import com.example.data.model.ShopProduct
import com.example.data.model.SoundType
import com.example.data.model.WheelReward
import com.example.data.local.db.WordGameDatabase
import com.example.data.repository.GameRepository
import com.example.data.repository.IWordGameRepository
import com.example.data.repository.WordGameRepository
import com.example.data.sound.SoundManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = GamePreferences(application)
    val soundManager = SoundManager(application)

    private val database = WordGameDatabase.getDatabase(application)
    val wordGameRepository: IWordGameRepository = WordGameRepository(
        levelDao = database.levelDao(),
        playerProgressDao = database.playerProgressDao(),
        gameScoreDao = database.gameScoreDao()
    )

    // Reactive streams from Repository
    val allLevels = wordGameRepository.allLevels
    val allProgress = wordGameRepository.allProgress
    val totalStars = wordGameRepository.totalStars
    val completedLevelsCount = wordGameRepository.completedLevelsCount

    // Navigation BackStack
    private val _route = MutableStateFlow(ScreenRoute.HOME)
    val route: StateFlow<ScreenRoute> = _route.asStateFlow()

    private val navigationStack = mutableListOf(ScreenRoute.HOME)

    // Wallet & Economy
    private val _coins = MutableStateFlow(prefs.coins)
    val coins: StateFlow<Int> = _coins.asStateFlow()

    private val _gems = MutableStateFlow(prefs.gems)
    val gems: StateFlow<Int> = _gems.asStateFlow()

    private val _bonusWordsCount = MutableStateFlow(prefs.bonusWordsCount)
    val bonusWordsCount: StateFlow<Int> = _bonusWordsCount.asStateFlow()

    private val _isVip = MutableStateFlow(prefs.isVip)
    val isVip: StateFlow<Boolean> = _isVip.asStateFlow()

    private val _isAdsRemoved = MutableStateFlow(prefs.isAdsRemoved)
    val isAdsRemoved: StateFlow<Boolean> = _isAdsRemoved.asStateFlow()

    // Settings
    private val _soundEnabled = MutableStateFlow(prefs.isSoundEnabled)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _vibrationEnabled = MutableStateFlow(prefs.isVibrationEnabled)
    val vibrationEnabled: StateFlow<Boolean> = _vibrationEnabled.asStateFlow()

    // Word Mode
    private val _currentLevelIndex = MutableStateFlow(prefs.currentLevel.coerceIn(1, GameRepository.levels.size))
    val currentLevelIndex: StateFlow<Int> = _currentLevelIndex.asStateFlow()

    private val _currentLevel = MutableStateFlow(GameRepository.levels[_currentLevelIndex.value - 1])
    val currentLevel: StateFlow<GameLevel> = _currentLevel.asStateFlow()

    private val _shuffledLetters = MutableStateFlow(_currentLevel.value.letters.shuffled())
    val shuffledLetters: StateFlow<List<Char>> = _shuffledLetters.asStateFlow()

    private val _selectedLetters = MutableStateFlow<List<Char>>(emptyList())
    val selectedLetters: StateFlow<List<Char>> = _selectedLetters.asStateFlow()

    private val _selectedLetterIndices = MutableStateFlow<List<Int>>(emptyList())
    val selectedLetterIndices: StateFlow<List<Int>> = _selectedLetterIndices.asStateFlow()

    private val _foundWords = MutableStateFlow<Set<String>>(emptySet())
    val foundWords: StateFlow<Set<String>> = _foundWords.asStateFlow()

    private val _revealedLetterIndices = MutableStateFlow<Map<String, Set<Int>>>(emptyMap())
    val revealedLetterIndices: StateFlow<Map<String, Set<Int>>> = _revealedLetterIndices.asStateFlow()

    private val _isLevelWon = MutableStateFlow(false)
    val isLevelWon: StateFlow<Boolean> = _isLevelWon.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Proverb Mode
    private val _currentProverbIndex = MutableStateFlow(prefs.currentProverbLevel.coerceIn(1, GameRepository.proverbs.size))
    val currentProverbIndex: StateFlow<Int> = _currentProverbIndex.asStateFlow()

    private val _currentProverb = MutableStateFlow(GameRepository.proverbs[_currentProverbIndex.value - 1])
    val currentProverb: StateFlow<ProverbLevel> = _currentProverb.asStateFlow()

    private val _proverbLetters = MutableStateFlow(getScrambledProverbLetters(_currentProverb.value))
    val proverbLetters: StateFlow<List<Char>> = _proverbLetters.asStateFlow()

    private val _selectedProverbIndices = MutableStateFlow<List<Int>>(emptyList())
    val selectedProverbIndices: StateFlow<List<Int>> = _selectedProverbIndices.asStateFlow()

    private val _proverbInput = MutableStateFlow("")
    val proverbInput: StateFlow<String> = _proverbInput.asStateFlow()

    private val _isProverbWon = MutableStateFlow(false)
    val isProverbWon: StateFlow<Boolean> = _isProverbWon.asStateFlow()

    // Daily & Streak
    val dailyChallenge: DailyChallenge = GameRepository.todayChallenge
    private val _dailyStreak = MutableStateFlow(prefs.dailyStreak)
    val dailyStreak: StateFlow<Int> = _dailyStreak.asStateFlow()

    private val _isDailyWon = MutableStateFlow(false)
    val isDailyWon: StateFlow<Boolean> = _isDailyWon.asStateFlow()

    // Fortune Wheel
    private val _wheelRotation = MutableStateFlow(0f)
    val wheelRotation: StateFlow<Float> = _wheelRotation.asStateFlow()

    private val _isSpinning = MutableStateFlow(false)
    val isSpinning: StateFlow<Boolean> = _isSpinning.asStateFlow()

    private val _lastWheelReward = MutableStateFlow<WheelReward?>(null)
    val lastWheelReward: StateFlow<WheelReward?> = _lastWheelReward.asStateFlow()

    private val _isDailyFreeSpinAvailable = MutableStateFlow(false)
    val isDailyFreeSpinAvailable: StateFlow<Boolean> = _isDailyFreeSpinAvailable.asStateFlow()

    private val _timeUntilNextFreeSpinMillis = MutableStateFlow(0L)
    val timeUntilNextFreeSpinMillis: StateFlow<Long> = _timeUntilNextFreeSpinMillis.asStateFlow()

    // Cafe Bazaar In-App Store & Ads Simulation
    private val _activeBazaarProduct = MutableStateFlow<ShopProduct?>(null)
    val activeBazaarProduct: StateFlow<ShopProduct?> = _activeBazaarProduct.asStateFlow()

    private val _bazaarPurchaseSuccess = MutableStateFlow<ShopProduct?>(null)
    val bazaarPurchaseSuccess: StateFlow<ShopProduct?> = _bazaarPurchaseSuccess.asStateFlow()

    private val _isWatchingRewardedAd = MutableStateFlow(false)
    val isWatchingRewardedAd: StateFlow<Boolean> = _isWatchingRewardedAd.asStateFlow()

    private val _adProgress = MutableStateFlow(0f)
    val adProgress: StateFlow<Float> = _adProgress.asStateFlow()

    // Leaderboard
    val leaderboardUsers: List<LeaderboardUser> = GameRepository.leaderboardUsers

    // 24-Hour Daily Login Bonus System
    private val _loginBonusDay = MutableStateFlow(prefs.loginBonusDay)
    val loginBonusDay: StateFlow<Int> = _loginBonusDay.asStateFlow()

    private val _isLoginBonusAvailable = MutableStateFlow(false)
    val isLoginBonusAvailable: StateFlow<Boolean> = _isLoginBonusAvailable.asStateFlow()

    private val _showDailyLoginDialog = MutableStateFlow(false)
    val showDailyLoginDialog: StateFlow<Boolean> = _showDailyLoginDialog.asStateFlow()

    private val _freeLetterHints = MutableStateFlow(prefs.freeLetterHints)
    val freeLetterHints: StateFlow<Int> = _freeLetterHints.asStateFlow()

    private val _freeWordHints = MutableStateFlow(prefs.freeWordHints)
    val freeWordHints: StateFlow<Int> = _freeWordHints.asStateFlow()

    private val _timeUntilNextBonusMillis = MutableStateFlow(0L)
    val timeUntilNextBonusMillis: StateFlow<Long> = _timeUntilNextBonusMillis.asStateFlow()

    init {
        soundManager.isSoundEnabled = prefs.isSoundEnabled
        soundManager.isVibrationEnabled = prefs.isVibrationEnabled
        loadLevelState(_currentLevelIndex.value)
        checkDailyLoginBonus()
        checkDailyFreeSpin()
        viewModelScope.launch {
            wordGameRepository.initializeDatabaseIfEmpty(GameRepository.levels)
        }
    }

    // --- Navigation ---
    fun navigateTo(route: ScreenRoute) {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        if (route == ScreenRoute.PROVERB_GAME) {
            initProverbLevel()
        }
        if (route == ScreenRoute.FORTUNE_WHEEL) {
            checkDailyFreeSpin()
        }
        if (route != _route.value) {
            navigationStack.add(route)
            _route.value = route
        }
    }

    fun handleBack(): Boolean {
        if (navigationStack.size > 1) {
            soundManager.playSound(SoundType.CLICK)
            soundManager.vibrate(SoundType.CLICK)
            navigationStack.removeAt(navigationStack.size - 1)
            _route.value = navigationStack.last()
            return true
        }
        return false
    }

    // --- Word Game Actions ---
    private fun loadLevelState(index: Int) {
        val level = GameRepository.levels[(index - 1).coerceIn(0, GameRepository.levels.size - 1)]
        _currentLevel.value = level
        _shuffledLetters.value = level.letters.shuffled()
        _selectedLetters.value = emptyList()
        _selectedLetterIndices.value = emptyList()
        _isLevelWon.value = false
        val savedWords = prefs.getFoundWords(level.id)
        _foundWords.value = savedWords
        _revealedLetterIndices.value = emptyMap()
    }

    fun selectWheelNodeIndex(index: Int) {
        val list = _shuffledLetters.value
        if (index !in list.indices) return
        if (_selectedLetterIndices.value.contains(index)) return
        soundManager.playSound(SoundType.LETTER_PICK)
        soundManager.vibrate(SoundType.LETTER_PICK)
        val newIndices = _selectedLetterIndices.value + index
        _selectedLetterIndices.value = newIndices
        _selectedLetters.value = newIndices.map { list[it] }
    }

    fun backtrackWheelNode() {
        if (_selectedLetterIndices.value.size > 1) {
            soundManager.playSound(SoundType.CLICK)
            soundManager.vibrate(SoundType.CLICK)
            val newIndices = _selectedLetterIndices.value.dropLast(1)
            _selectedLetterIndices.value = newIndices
            _selectedLetters.value = newIndices.map { _shuffledLetters.value[it] }
        } else if (_selectedLetterIndices.value.size == 1) {
            soundManager.playSound(SoundType.CLICK)
            soundManager.vibrate(SoundType.CLICK)
            _selectedLetterIndices.value = emptyList()
            _selectedLetters.value = emptyList()
        }
    }

    fun onLetterTapped(letter: Char) {
        val list = _shuffledLetters.value
        val availableIdx = list.indices.firstOrNull { idx ->
            list[idx] == letter && !_selectedLetterIndices.value.contains(idx)
        }
        if (availableIdx != null) {
            selectWheelNodeIndex(availableIdx)
        }
    }

    fun clearSelectedLetters() {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        _selectedLetters.value = emptyList()
        _selectedLetterIndices.value = emptyList()
    }

    fun shuffleLetters() {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        _selectedLetters.value = emptyList()
        _selectedLetterIndices.value = emptyList()
        _shuffledLetters.value = _shuffledLetters.value.shuffled()
    }

    fun submitCurrentWord() {
        val formedWord = _selectedLetters.value.joinToString("")
        val level = _currentLevel.value

        if (formedWord.isEmpty()) return

        _selectedLetters.value = emptyList()
        _selectedLetterIndices.value = emptyList()

        when {
            level.targetWords.contains(formedWord) -> {
                if (_foundWords.value.contains(formedWord)) {
                    soundManager.playSound(SoundType.ERROR)
                    soundManager.vibrate(SoundType.ERROR)
                    showToast("کلمه «$formedWord» قبلاً پیدا شده است!")
                } else {
                    val updated = _foundWords.value + formedWord
                    _foundWords.value = updated
                    prefs.saveFoundWord(level.id, formedWord)
                    viewModelScope.launch {
                        wordGameRepository.saveFoundWord(level.id, formedWord)
                    }
                    soundManager.playSound(SoundType.WORD_FOUND)
                    soundManager.vibrate(SoundType.WORD_FOUND)
                    addCoins(25)

                    if (updated.size >= level.targetWords.size) {
                        // Level complete!
                        _isLevelWon.value = true
                        prefs.saveLevelStars(level.id, 3)
                        soundManager.playSound(SoundType.LEVEL_WON)
                        soundManager.vibrate(SoundType.LEVEL_WON)
                        addCoins(level.rewardCoins)
                        addGems(level.rewardGems)
                        if (_currentLevelIndex.value < GameRepository.levels.size) {
                            prefs.currentLevel = _currentLevelIndex.value + 1
                        }
                        viewModelScope.launch {
                            wordGameRepository.completeLevel(
                                levelId = level.id,
                                stars = 3,
                                score = 150,
                                coinsEarned = level.rewardCoins,
                                gemsEarned = level.rewardGems,
                                totalLevelsCount = GameRepository.levels.size
                            )
                        }
                    }
                }
            }
            level.bonusWords.contains(formedWord) -> {
                soundManager.playSound(SoundType.COIN_EARNED)
                soundManager.vibrate(SoundType.COIN_EARNED)
                val newCount = _bonusWordsCount.value + 1
                _bonusWordsCount.value = newCount
                prefs.bonusWordsCount = newCount
                addCoins(10)
                showToast("آفرین! کلمه اضافه «$formedWord» وارد قلک شد (+۱۰ سکه) 🏺")
            }
            else -> {
                soundManager.playSound(SoundType.ERROR)
                soundManager.vibrate(SoundType.ERROR)
                showToast("کلمه «$formedWord» در جدول وجود ندارد")
            }
        }
        _selectedLetters.value = emptyList()
    }

    fun useHintRevealLetter() {
        val level = _currentLevel.value
        val uncompleted = level.targetWords.filter { !_foundWords.value.contains(it) }
        if (uncompleted.isEmpty()) return

        val target = uncompleted.first()
        val currentRevealed = _revealedLetterIndices.value[target] ?: emptySet()
        val unrevealedIndices = (0 until target.length).filter { !currentRevealed.contains(it) }
        if (unrevealedIndices.isEmpty()) return

        // 1. Check if user has free letter hint power-up from daily login bonus!
        if (_freeLetterHints.value > 0) {
            val updated = _freeLetterHints.value - 1
            _freeLetterHints.value = updated
            prefs.freeLetterHints = updated
            soundManager.playSound(SoundType.COIN_EARNED)
            soundManager.vibrate(SoundType.COIN_EARNED)
            val revealIndex = unrevealedIndices.random()
            val newMap = _revealedLetterIndices.value.toMutableMap()
            newMap[target] = currentRevealed + revealIndex
            _revealedLetterIndices.value = newMap
            showToast("از پاداش ورود: ۱ راهنمای حرف رایگان مصرف شد! 💡")
            return
        }

        if (_coins.value < 50) {
            soundManager.playSound(SoundType.ERROR)
            soundManager.vibrate(SoundType.ERROR)
            showToast("سکه کافی ندارید! (نیاز به ۵۰ سکه)")
            navigateTo(ScreenRoute.BAZAAR_STORE)
            return
        }

        spendCoins(50)
        soundManager.playSound(SoundType.COIN_EARNED)
        soundManager.vibrate(SoundType.COIN_EARNED)
        val revealIndex = unrevealedIndices.random()
        val newMap = _revealedLetterIndices.value.toMutableMap()
        newMap[target] = currentRevealed + revealIndex
        _revealedLetterIndices.value = newMap
        showToast("یک حرف از کلمه فاش شد! ✨")
    }

    fun useHintRevealWord() {
        val level = _currentLevel.value
        val uncompleted = level.targetWords.filter { !_foundWords.value.contains(it) }
        if (uncompleted.isEmpty()) return

        // 1. Check if user has free word hint power-up from daily login bonus!
        if (_freeWordHints.value > 0) {
            val updatedHints = _freeWordHints.value - 1
            _freeWordHints.value = updatedHints
            prefs.freeWordHints = updatedHints
            val target = uncompleted.first()
            val updated = _foundWords.value + target
            _foundWords.value = updated
            prefs.saveFoundWord(level.id, target)
            viewModelScope.launch {
                wordGameRepository.saveFoundWord(level.id, target)
            }
            soundManager.playSound(SoundType.WORD_FOUND)
            soundManager.vibrate(SoundType.WORD_FOUND)
            showToast("از پاداش ورود: کلمه «$target» به رایگان فاش شد! 🔮")

            if (updated.size >= level.targetWords.size) {
                _isLevelWon.value = true
                soundManager.playSound(SoundType.LEVEL_WON)
                soundManager.vibrate(SoundType.LEVEL_WON)
                addCoins(level.rewardCoins)
                addGems(level.rewardGems)
                if (_currentLevelIndex.value < GameRepository.levels.size) {
                    prefs.currentLevel = _currentLevelIndex.value + 1
                }
                viewModelScope.launch {
                    wordGameRepository.completeLevel(
                        levelId = level.id,
                        stars = 3,
                        score = 150,
                        coinsEarned = level.rewardCoins,
                        gemsEarned = level.rewardGems,
                        totalLevelsCount = GameRepository.levels.size
                    )
                }
            }
            return
        }

        if (_coins.value < 120) {
            soundManager.playSound(SoundType.ERROR)
            soundManager.vibrate(SoundType.ERROR)
            showToast("سکه کافی ندارید! (نیاز به ۱۲۰ سکه)")
            navigateTo(ScreenRoute.BAZAAR_STORE)
            return
        }

        spendCoins(120)
        val target = uncompleted.first()
        val updated = _foundWords.value + target
        _foundWords.value = updated
        prefs.saveFoundWord(level.id, target)
        viewModelScope.launch {
            wordGameRepository.saveFoundWord(level.id, target)
        }
        soundManager.playSound(SoundType.WORD_FOUND)
        soundManager.vibrate(SoundType.WORD_FOUND)
        showToast("کلمه «$target» کامل فاش شد! 💡")

        if (updated.size >= level.targetWords.size) {
            _isLevelWon.value = true
            soundManager.playSound(SoundType.LEVEL_WON)
            soundManager.vibrate(SoundType.LEVEL_WON)
            addCoins(level.rewardCoins)
            addGems(level.rewardGems)
            if (_currentLevelIndex.value < GameRepository.levels.size) {
                prefs.currentLevel = _currentLevelIndex.value + 1
            }
            viewModelScope.launch {
                wordGameRepository.completeLevel(
                    levelId = level.id,
                    stars = 3,
                    score = 150,
                    coinsEarned = level.rewardCoins,
                    gemsEarned = level.rewardGems,
                    totalLevelsCount = GameRepository.levels.size
                )
            }
        }
    }

    fun goToNextLevel() {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        if (_currentLevelIndex.value < GameRepository.levels.size) {
            val nextIdx = _currentLevelIndex.value + 1
            _currentLevelIndex.value = nextIdx
            loadLevelState(nextIdx)
        } else {
            showToast("تبریک! شما همه مراحل قند پارسی را فتح کردید! 🏆")
            navigateTo(ScreenRoute.HOME)
        }
    }

    fun selectLevelFromMap(levelId: Int) {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        _currentLevelIndex.value = levelId
        loadLevelState(levelId)
        navigateTo(ScreenRoute.WORD_GAME)
    }

    private fun getScrambledProverbLetters(level: ProverbLevel): List<Char> {
        val list = level.scrambledLetters.toMutableList()
        var shuffled = list.shuffled()
        var attempts = 0
        while (shuffled.joinToString("").contains(level.missingWord) && attempts < 15) {
            shuffled = list.shuffled()
            attempts++
        }
        return shuffled
    }

    fun initProverbLevel() {
        val current = _currentProverb.value
        _proverbLetters.value = getScrambledProverbLetters(current)
        _proverbInput.value = ""
        _selectedProverbIndices.value = emptyList()
        _isProverbWon.value = false
    }

    // --- Proverb Game Actions ---
    fun onProverbLetterTapped(index: Int, char: Char) {
        val current = _currentProverb.value
        if (_selectedProverbIndices.value.contains(index)) return
        if (_proverbInput.value.length < current.missingWord.length) {
            soundManager.playSound(SoundType.LETTER_PICK)
            soundManager.vibrate(SoundType.LETTER_PICK)
            _selectedProverbIndices.value = _selectedProverbIndices.value + index
            _proverbInput.value += char
        }
    }

    fun onRemoveProverbLetterAt(slotIndex: Int) {
        if (slotIndex < _proverbInput.value.length && slotIndex < _selectedProverbIndices.value.size) {
            soundManager.playSound(SoundType.CLICK)
            soundManager.vibrate(SoundType.CLICK)
            val inputBuilder = StringBuilder(_proverbInput.value)
            inputBuilder.deleteCharAt(slotIndex)
            _proverbInput.value = inputBuilder.toString()
            val indicesList = _selectedProverbIndices.value.toMutableList()
            indicesList.removeAt(slotIndex)
            _selectedProverbIndices.value = indicesList
        }
    }

    fun clearProverbInput() {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        _proverbInput.value = ""
        _selectedProverbIndices.value = emptyList()
    }

    fun onProverbBackspace() {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        if (_proverbInput.value.isNotEmpty() && _selectedProverbIndices.value.isNotEmpty()) {
            _proverbInput.value = _proverbInput.value.dropLast(1)
            _selectedProverbIndices.value = _selectedProverbIndices.value.dropLast(1)
        }
    }

    fun shuffleProverbLetters() {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        _proverbLetters.value = getScrambledProverbLetters(_currentProverb.value)
        _proverbInput.value = ""
        _selectedProverbIndices.value = emptyList()
    }

    fun submitProverbAnswer() {
        val current = _currentProverb.value
        if (_proverbInput.value == current.missingWord) {
            soundManager.playSound(SoundType.LEVEL_WON)
            soundManager.vibrate(SoundType.LEVEL_WON)
            _isProverbWon.value = true
            addCoins(current.rewardCoins)
            addGems(1)
            if (_currentProverbIndex.value < GameRepository.proverbs.size) {
                prefs.currentProverbLevel = _currentProverbIndex.value + 1
            }
        } else {
            soundManager.playSound(SoundType.ERROR)
            soundManager.vibrate(SoundType.ERROR)
            showToast("پاسخ درست نیست، دوباره تلاش کن!")
            _proverbInput.value = ""
            _selectedProverbIndices.value = emptyList()
        }
    }

    fun nextProverbLevel() {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        if (_currentProverbIndex.value < GameRepository.proverbs.size) {
            val nextIdx = _currentProverbIndex.value + 1
            _currentProverbIndex.value = nextIdx
            val nextLevel = GameRepository.proverbs[nextIdx - 1]
            _currentProverb.value = nextLevel
            _proverbLetters.value = getScrambledProverbLetters(nextLevel)
            _proverbInput.value = ""
            _selectedProverbIndices.value = emptyList()
            _isProverbWon.value = false
        } else {
            showToast("تمام ضرب‌المثل‌ها را با موفقیت حل کردید! 🎖️")
            navigateTo(ScreenRoute.HOME)
        }
    }

    // --- Lucky Spin Fortune Wheel (۲۴ ساعته با جوایز سکه و راهنما) ---
    fun checkDailyFreeSpin() {
        val lastSpin = prefs.lastWheelSpinTime
        val now = System.currentTimeMillis()
        if (lastSpin == 0L || (now - lastSpin) >= 24 * 60 * 60 * 1000L) {
            _isDailyFreeSpinAvailable.value = true
            _timeUntilNextFreeSpinMillis.value = 0L
        } else {
            _isDailyFreeSpinAvailable.value = false
            _timeUntilNextFreeSpinMillis.value = (24 * 60 * 60 * 1000L) - (now - lastSpin)
        }
    }

    fun spinFortuneWheel(isFreeSpinFromAd: Boolean = false) {
        if (_isSpinning.value) return
        checkDailyFreeSpin()

        val isDailyFree = _isDailyFreeSpinAvailable.value
        val cost = when {
            isDailyFree -> 0
            isFreeSpinFromAd -> 0
            else -> 50
        }

        if (cost > 0 && _coins.value < cost) {
            soundManager.playSound(SoundType.ERROR)
            soundManager.vibrate(SoundType.ERROR)
            showToast("برای چرخش گردونه حداقل ۵۰ سکه لازم است، یا می‌توانید ویدیوی رایگان ببینید!")
            return
        }

        if (cost > 0) {
            spendCoins(cost)
        }

        val now = System.currentTimeMillis()
        if (isDailyFree) {
            prefs.lastWheelSpinTime = now
            _isDailyFreeSpinAvailable.value = false
            _timeUntilNextFreeSpinMillis.value = 24 * 60 * 60 * 1000L
        }

        _isSpinning.value = true
        _lastWheelReward.value = null

        viewModelScope.launch {
            val rewardsCount = GameRepository.wheelRewards.size
            val targetIndex = Random.nextInt(rewardsCount)

            // Exact mathematical angle alignment:
            // Slices start from 0 at 3 o'clock. Pointer needle is at top (270 degrees).
            // Segment k center is at k * segmentAngle + (segmentAngle / 2)
            val segmentAngle = 360f / rewardsCount
            val segmentCenter = targetIndex * segmentAngle + (segmentAngle / 2f)
            val desiredModulo = (270f - segmentCenter + 3600f) % 360f

            val currentRot = _wheelRotation.value
            val currentModulo = (currentRot % 360f + 360f) % 360f
            var delta = desiredModulo - currentModulo
            if (delta < 0f) delta += 360f

            // Add 6 full rotations (2160 degrees) for smooth realistic spin duration
            val totalDelta = 360f * 6 + delta
            val targetRotation = currentRot + totalDelta

            _wheelRotation.value = targetRotation

            // Realistic deceleration audio/haptic ticks simulating peg strikes
            val tickDelays = listOf(60L, 70L, 75L, 80L, 90L, 105L, 125L, 150L, 180L, 220L, 270L, 330L, 400L, 480L)
            for (d in tickDelays) {
                delay(d)
                soundManager.playSound(SoundType.WHEEL_SPIN)
                soundManager.vibrate(SoundType.WHEEL_SPIN)
            }

            delay(280) // Let the animation settle smoothly on the slice

            val wonReward = GameRepository.wheelRewards[targetIndex]
            _lastWheelReward.value = wonReward
            _isSpinning.value = false

            if (wonReward.coins > 0) addCoins(wonReward.coins)
            if (wonReward.gems > 0) addGems(wonReward.gems)
            if (wonReward.freeLetterHints > 0) {
                val updated = prefs.freeLetterHints + wonReward.freeLetterHints
                prefs.freeLetterHints = updated
                _freeLetterHints.value = updated
            }
            if (wonReward.freeWordHints > 0) {
                val updated = prefs.freeWordHints + wonReward.freeWordHints
                prefs.freeWordHints = updated
                _freeWordHints.value = updated
            }

            soundManager.playSound(SoundType.LEVEL_WON)
            soundManager.vibrate(SoundType.LEVEL_WON)

            val summary = buildString {
                append("تبریک! ${wonReward.labelFa} برنده شدید: ")
                if (wonReward.coins > 0) append("+${wonReward.coins} سکه ")
                if (wonReward.gems > 0) append("+${wonReward.gems} الماس ")
                if (wonReward.freeLetterHints > 0) append("+${wonReward.freeLetterHints} راهنمای حرف 💡 ")
                if (wonReward.freeWordHints > 0) append("+${wonReward.freeWordHints} فاش کلمه 🔮 ")
            }
            showToast(summary)
        }
    }

    fun dismissWheelReward() {
        soundManager.playSound(SoundType.CLICK)
        _lastWheelReward.value = null
    }

    fun watchAdForFortuneSpin() {
        if (_isSpinning.value || _isWatchingRewardedAd.value) return
        _isWatchingRewardedAd.value = true
        _adProgress.value = 0f

        viewModelScope.launch {
            for (step in 1..20) {
                delay(70)
                _adProgress.value = step / 20f
            }
            _isWatchingRewardedAd.value = false
            showToast("ویدیوی بازار دیده شد! چرخش رایگان فعال شد 🎬✨")
            spinFortuneWheel(isFreeSpinFromAd = true)
        }
    }

    // --- Cafe Bazaar In-App Store & Monetization ---
    fun openBazaarPayment(product: ShopProduct) {
        soundManager.playSound(SoundType.CLICK)
        soundManager.vibrate(SoundType.CLICK)
        _activeBazaarProduct.value = product
    }

    fun dismissBazaarDialog() {
        soundManager.playSound(SoundType.CLICK)
        _activeBazaarProduct.value = null
    }

    fun confirmBazaarPurchase(product: ShopProduct) {
        viewModelScope.launch {
            soundManager.playSound(SoundType.CLICK)
            delay(500) // Simulate Cafe Bazaar gateway handshake

            addCoins(product.coins)
            addGems(product.gems)

            if (product.isVip) {
                prefs.isVip = true
                _isVip.value = true
            }
            if (product.id == "bazaar_royal_box") {
                prefs.isAdsRemoved = true
                _isAdsRemoved.value = true
            }

            _activeBazaarProduct.value = null
            _bazaarPurchaseSuccess.value = product
            soundManager.playSound(SoundType.LEVEL_WON)
            soundManager.vibrate(SoundType.LEVEL_WON)
        }
    }

    fun dismissPurchaseSuccess() {
        soundManager.playSound(SoundType.CLICK)
        _bazaarPurchaseSuccess.value = null
    }

    fun watchRewardedVideoAd() {
        if (_isWatchingRewardedAd.value) return
        _isWatchingRewardedAd.value = true
        _adProgress.value = 0f

        viewModelScope.launch {
            for (step in 1..30) {
                delay(100)
                _adProgress.value = step / 30f
            }
            _isWatchingRewardedAd.value = false
            addCoins(60)
            addGems(1)
            soundManager.playSound(SoundType.LEVEL_WON)
            soundManager.vibrate(SoundType.LEVEL_WON)
            showToast("جایزه ویدیوی تبلیغاتی بازار دریافت شد: ۶۰ سکه + ۱ الماس! 🎁")
        }
    }

    // --- Piggy Bank ---
    fun smashPiggyBank() {
        if (_bonusWordsCount.value < 20) {
            soundManager.playSound(SoundType.ERROR)
            showToast("قلک کلمات هنوز پر نشده است! (حداقل ۲۰ کلمه لازم است)")
            return
        }
        val reward = _bonusWordsCount.value * 5
        addCoins(reward)
        _bonusWordsCount.value = 0
        prefs.bonusWordsCount = 0
        soundManager.playSound(SoundType.LEVEL_WON)
        soundManager.vibrate(SoundType.LEVEL_WON)
        showToast("قلک شکسته شد و $reward سکه طلایی دریافت کردید! 🏺💎")
    }

    // --- Settings Toggles ---
    fun toggleSound() {
        val newState = !_soundEnabled.value
        _soundEnabled.value = newState
        prefs.isSoundEnabled = newState
        soundManager.isSoundEnabled = newState
    }

    fun toggleVibration() {
        val newState = !_vibrationEnabled.value
        _vibrationEnabled.value = newState
        prefs.isVibrationEnabled = newState
        soundManager.isVibrationEnabled = newState
    }

    // --- 24-Hour Daily Login Bonus System ---
    fun checkDailyLoginBonus() {
        val lastClaim = prefs.lastLoginBonusTime
        val now = System.currentTimeMillis()
        if (lastClaim == 0L) {
            _isLoginBonusAvailable.value = true
            _timeUntilNextBonusMillis.value = 0L
            _showDailyLoginDialog.value = true
        } else {
            val elapsed = now - lastClaim
            if (elapsed >= 24 * 60 * 60 * 1000L) {
                if (elapsed > 48 * 60 * 60 * 1000L) {
                    prefs.loginBonusDay = 1
                    _loginBonusDay.value = 1
                }
                _isLoginBonusAvailable.value = true
                _timeUntilNextBonusMillis.value = 0L
                _showDailyLoginDialog.value = true
            } else {
                _isLoginBonusAvailable.value = false
                _timeUntilNextBonusMillis.value = (24 * 60 * 60 * 1000L) - elapsed
            }
        }
    }

    fun openDailyLoginDialog() {
        checkDailyLoginBonus()
        soundManager.playSound(SoundType.CLICK)
        _showDailyLoginDialog.value = true
    }

    fun dismissDailyLoginDialog() {
        soundManager.playSound(SoundType.CLICK)
        _showDailyLoginDialog.value = false
    }

    fun claimDailyLoginBonus(doubleWithAd: Boolean = false) {
        val now = System.currentTimeMillis()
        val lastClaim = prefs.lastLoginBonusTime
        val elapsed = now - lastClaim

        if (lastClaim != 0L && elapsed < 24 * 60 * 60 * 1000L) {
            showToast("پاداش امروز قبلاً دریافت شده است!")
            return
        }

        if (lastClaim != 0L && elapsed > 48 * 60 * 60 * 1000L) {
            prefs.loginBonusDay = 1
            _loginBonusDay.value = 1
        }

        val currentDay = _loginBonusDay.value
        val reward = GameRepository.dailyLoginRewards.firstOrNull { it.dayNumber == currentDay }
            ?: GameRepository.dailyLoginRewards[0]

        val multiplier = if (doubleWithAd) 2 else 1
        val earnedCoins = reward.coins * multiplier
        val earnedGems = reward.gems * multiplier
        val earnedLetterHints = reward.freeLetterHints * multiplier
        val earnedWordHints = reward.freeWordHints * multiplier

        addCoins(earnedCoins)
        if (earnedGems > 0) addGems(earnedGems)

        if (earnedLetterHints > 0) {
            val updated = prefs.freeLetterHints + earnedLetterHints
            prefs.freeLetterHints = updated
            _freeLetterHints.value = updated
        }
        if (earnedWordHints > 0) {
            val updated = prefs.freeWordHints + earnedWordHints
            prefs.freeWordHints = updated
            _freeWordHints.value = updated
        }

        prefs.lastLoginBonusTime = now
        val nextDay = if (currentDay >= 7) 1 else currentDay + 1
        prefs.loginBonusDay = nextDay
        _loginBonusDay.value = nextDay
        _isLoginBonusAvailable.value = false
        _timeUntilNextBonusMillis.value = 24 * 60 * 60 * 1000L

        soundManager.playSound(SoundType.LEVEL_WON)
        soundManager.vibrate(SoundType.LEVEL_WON)

        val summary = buildString {
            append("پاداش ${reward.dayTitleFa} دریافت شد: ")
            append("+$earnedCoins سکه ")
            if (earnedGems > 0) append("+$earnedGems الماس ")
            if (earnedLetterHints > 0) append("+$earnedLetterHints راهنمای حرف ")
            if (earnedWordHints > 0) append("+$earnedWordHints فاش کلمه ")
            if (doubleWithAd) append("(۲ برابر شده با ویدیو بازار! 🎁)")
        }
        showToast(summary)
    }

    // --- Helpers ---
    private fun addCoins(amount: Int) {
        val updated = _coins.value + amount
        _coins.value = updated
        prefs.coins = updated
    }

    private fun spendCoins(amount: Int) {
        val updated = (_coins.value - amount).coerceAtLeast(0)
        _coins.value = updated
        prefs.coins = updated
    }

    private fun addGems(amount: Int) {
        val updated = _gems.value + amount
        _gems.value = updated
        prefs.gems = updated
    }

    private fun showToast(msg: String) {
        _toastMessage.value = msg
        viewModelScope.launch {
            delay(2800)
            if (_toastMessage.value == msg) {
                _toastMessage.value = null
            }
        }
    }
}
