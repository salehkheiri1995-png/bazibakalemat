package com.aistudio.qandparsi.data.repository

import com.aistudio.qandparsi.data.model.DailyChallenge
import com.aistudio.qandparsi.data.model.GameLevel
import com.aistudio.qandparsi.data.model.GemExchangeOffer
import com.aistudio.qandparsi.data.model.LeaderboardUser
import com.aistudio.qandparsi.data.model.ProverbLevel
import com.aistudio.qandparsi.data.model.ShopProduct
import com.aistudio.qandparsi.data.model.WheelReward
import com.aistudio.qandparsi.util.LetterScrambler

object GameRepository {

    // 35 Handcrafted Levels spanning 7 major Iranian tourist/cultural hubs. They are kept as
    // the first five stages of their city and the rest of the map is generated below.
    private val handcraftedLevels: List<GameLevel> = listOf(
        // شهر ۱: تهران - کاخ گلستان و عمارت مسعودیه
        GameLevel(
            id = 1,
            city = "tehran",
            cityNameFa = "تهران - کاخ گلستان",
            stageNumber = 1,
            letters = listOf('س', 'ل', 'ا', 'م'),
            targetWords = listOf("سلام", "مال", "لمس"),
            bonusWords = listOf("سالم", "مس", "سم")
        ),
        GameLevel(
            id = 2,
            city = "tehran",
            cityNameFa = "تهران - عمارت مسعودیه",
            stageNumber = 2,
            letters = listOf('ک', 'ا', 'ر', 'د'),
            targetWords = listOf("کارد", "اردک", "کادر", "دراک"),
            bonusWords = listOf("راکد", "درک", "کدر")
        ),
        GameLevel(
            id = 3,
            city = "tehran",
            cityNameFa = "تهران - برج میلاد",
            stageNumber = 3,
            letters = listOf('ب', 'ا', 'ز', 'ا', 'ر'),
            targetWords = listOf("بازار", "بارز", "ابزار", "بار"),
            bonusWords = listOf("راز", "باز", "زار")
        ),
        GameLevel(
            id = 4,
            city = "tehran",
            cityNameFa = "تهران - دربند و توچال",
            stageNumber = 4,
            letters = listOf('ق', 'ن', 'د', 'و'),
            targetWords = listOf("قند", "نقد", "نو"),
            bonusWords = listOf("دو", "قو")
        ),
        GameLevel(
            id = 5,
            city = "tehran",
            cityNameFa = "تهران - بازار بزرگ",
            stageNumber = 5,
            letters = listOf('ش', 'ک', 'ر', 'ا', 'ن'),
            targetWords = listOf("شکر", "شکار", "کنار", "کار", "نار"),
            bonusWords = listOf("کاش", "رشک", "کان")
        ),

        // شهر ۲: شیراز - پایتخت شعر و ادب
        GameLevel(
            id = 6,
            city = "shiraz",
            cityNameFa = "شیراز - آرامگاه حافظ",
            stageNumber = 6,
            letters = listOf('غ', 'ز', 'ل'),
            targetWords = listOf("غزل", "لغز"),
            bonusWords = listOf("غل")
        ),
        GameLevel(
            id = 7,
            city = "shiraz",
            cityNameFa = "شیراز - باغ ارم",
            stageNumber = 7,
            letters = listOf('ب', 'ه', 'ا', 'ر'),
            targetWords = listOf("بهار", "راه", "ابر", "بار"),
            bonusWords = listOf("بره", "هر", "به")
        ),
        GameLevel(
            id = 8,
            city = "shiraz",
            cityNameFa = "شیراز - سعدیه",
            stageNumber = 8,
            letters = listOf('ش', 'ع', 'ر', 'ا', 'ا'),
            targetWords = listOf("شعر", "شعرا", "عرش", "شرع"),
            bonusWords = listOf("شعار", "عار", "رعش")
        ),
        GameLevel(
            id = 9,
            city = "shiraz",
            cityNameFa = "شیراز - شاهچراغ",
            stageNumber = 9,
            letters = listOf('ن', 'ب', 'ا', 'ت'),
            targetWords = listOf("نبات", "تاب", "ناب", "تن"),
            bonusWords = listOf("بنات", "تب", "بت")
        ),
        GameLevel(
            id = 10,
            city = "shiraz",
            cityNameFa = "شیراز - تخت جمشید",
            stageNumber = 10,
            letters = listOf('ش', 'ی', 'ر', 'ا', 'ز'),
            targetWords = listOf("شیراز", "ریزش", "شیر", "راز", "ریش"),
            bonusWords = listOf("زار", "ریز", "یار")
        ),

        // شهر ۳: اصفهان - نصف جهان
        GameLevel(
            id = 11,
            city = "isfahan",
            cityNameFa = "اصفهان - میدان نقش جهان",
            stageNumber = 11,
            letters = listOf('ه', 'ن', 'ر'),
            targetWords = listOf("هنر", "نهر", "رهن"),
            bonusWords = listOf("هر", "نه")
        ),
        GameLevel(
            id = 12,
            city = "isfahan",
            cityNameFa = "اصفهان - سی و سه پل",
            stageNumber = 12,
            letters = listOf('ق', 'ل', 'م', 'ه'),
            targetWords = listOf("قلم", "قلمه", "هم"),
            bonusWords = listOf("مه")
        ),
        GameLevel(
            id = 13,
            city = "isfahan",
            cityNameFa = "اصفهان - پل خواجو",
            stageNumber = 13,
            letters = listOf('گ', 'ن', 'ب', 'د'),
            targetWords = listOf("گنبد", "بند", "دنگ"),
            bonusWords = listOf("گند", "بد", "بن")
        ),
        GameLevel(
            id = 14,
            city = "isfahan",
            cityNameFa = "اصفهان - عالی‌قاپو",
            stageNumber = 14,
            letters = listOf('ک', 'ا', 'ش', 'ی'),
            targetWords = listOf("کاشی", "شیک", "کیش"),
            bonusWords = listOf("شک", "کیا", "کش")
        ),
        GameLevel(
            id = 15,
            city = "isfahan",
            cityNameFa = "اصفهان - مسجد شیخ لطف‌الله",
            stageNumber = 15,
            letters = listOf('ف', 'ی', 'ر', 'و', 'ز', 'ه'),
            targetWords = listOf("فیروزه", "روزه", "فیروز", "زیره", "زور"),
            bonusWords = listOf("روزی", "روز", "زیر", "روی")
        ),

        // شهر ۴: یزد - شهر خشت خام و بادگیرها
        GameLevel(
            id = 16,
            city = "yazd",
            cityNameFa = "یزد - میدان امیرچخماق",
            stageNumber = 16,
            letters = listOf('خ', 'ش', 'ت', 'ر'),
            targetWords = listOf("خشت", "شتر", "رخت", "ترش"),
            bonusWords = listOf("خر", "شر", "تر")
        ),
        GameLevel(
            id = 17,
            city = "yazd",
            cityNameFa = "یزد - قنات زارچ",
            stageNumber = 17,
            letters = listOf('ق', 'ن', 'ا', 'ت', 'ر'),
            targetWords = listOf("قنات", "تار", "ران", "نار"),
            bonusWords = listOf("تر", "نت", "تن")
        ),
        GameLevel(
            id = 18,
            city = "yazd",
            cityNameFa = "یزد - باغ دولت‌آباد",
            stageNumber = 18,
            letters = listOf('ک', 'و', 'ی', 'ر', 'ز'),
            targetWords = listOf("کویر", "وزیر", "کوری", "زیر", "زور"),
            bonusWords = listOf("ریز", "روی", "کور")
        ),
        GameLevel(
            id = 19,
            city = "yazd",
            cityNameFa = "یزد - دخمه زرتشتیان",
            stageNumber = 19,
            letters = listOf('س', 'ف', 'ا', 'ل'),
            targetWords = listOf("سفال", "فلس", "سال", "فال", "الف"),
            bonusWords = listOf("لاف", "سلف")
        ),
        GameLevel(
            id = 20,
            city = "yazd",
            cityNameFa = "یزد - آتشکده بهرام",
            stageNumber = 20,
            letters = listOf('ب', 'ا', 'د', 'گ', 'ی', 'ر'),
            targetWords = listOf("بادگیر", "گرداب", "بیدار", "دریا", "برگ"),
            bonusWords = listOf("دیبا", "دبیر", "بید", "گاری")
        ),

        // شهر ۵: گیلان - بهشت سبز و بارانی
        GameLevel(
            id = 21,
            city = "gilan",
            cityNameFa = "گیلان - شالیزارهای رشت",
            stageNumber = 21,
            letters = listOf('ب', 'ر', 'ن', 'ج'),
            targetWords = listOf("برنج", "رنج", "برج"),
            bonusWords = listOf("جبر", "جنب")
        ),
        GameLevel(
            id = 22,
            city = "gilan",
            cityNameFa = "گیلان - جنگل‌های گیسوم",
            stageNumber = 22,
            letters = listOf('ب', 'ا', 'ر', 'ا', 'ن'),
            targetWords = listOf("باران", "انبار", "انار", "بار"),
            bonusWords = listOf("برنا", "نار", "ران")
        ),
        GameLevel(
            id = 23,
            city = "gilan",
            cityNameFa = "گیلان - روستای پلکانی ماسوله",
            stageNumber = 23,
            letters = listOf('د', 'ر', 'ی', 'ا'),
            targetWords = listOf("دریا", "دیر", "یار", "دایر"),
            bonusWords = listOf("رای", "دار")
        ),
        GameLevel(
            id = 24,
            city = "gilan",
            cityNameFa = "گیلان - تالاب انزلی",
            stageNumber = 24,
            letters = listOf('ج', 'ن', 'گ', 'ل'),
            targetWords = listOf("جنگل", "لجن", "گنج"),
            bonusWords = listOf("لگن", "جنگ")
        ),
        GameLevel(
            id = 25,
            city = "gilan",
            cityNameFa = "گیلان - قلعه رودخان",
            stageNumber = 25,
            letters = listOf('م', 'ا', 'س', 'و', 'ل', 'ه'),
            targetWords = listOf("ماسوله", "سوله", "سالم", "سلام", "ماه"),
            bonusWords = listOf("مال", "مولا", "هوس", "واله", "لوس")
        ),

        // شهر ۶: مشهد - پایتخت معنوی و زعفران
        GameLevel(
            id = 26,
            city = "mashhad",
            cityNameFa = "مشهد - حرم مطهر",
            stageNumber = 26,
            letters = listOf('ح', 'ر', 'م'),
            targetWords = listOf("حرم", "رحم", "حر"),
            bonusWords = listOf("رم")
        ),
        GameLevel(
            id = 27,
            city = "mashhad",
            cityNameFa = "مشهد - آرامگاه فردوسی طوس",
            stageNumber = 27,
            letters = listOf('ط', 'و', 'س', 'ت'),
            targetWords = listOf("طوس", "وسط", "تو", "سوت"),
            bonusWords = listOf("ست", "توسط")
        ),
        GameLevel(
            id = 28,
            city = "mashhad",
            cityNameFa = "مشهد - بازار رضا",
            stageNumber = 28,
            letters = listOf('گ', 'و', 'ه', 'ر'),
            targetWords = listOf("گوهر", "گروه", "گهر", "هور"),
            bonusWords = listOf("رگ", "گرو", "هر")
        ),
        GameLevel(
            id = 29,
            city = "mashhad",
            cityNameFa = "مشهد - طرقبه و شاندیز",
            stageNumber = 29,
            letters = listOf('ف', 'ی', 'ر', 'و', 'ز'),
            targetWords = listOf("فیروز", "فوری", "روزی", "زور"),
            bonusWords = listOf("زیور", "روز", "زیر", "ریز")
        ),
        GameLevel(
            id = 30,
            city = "mashhad",
            cityNameFa = "مشهد - مزارع طلای سرخ",
            stageNumber = 30,
            letters = listOf('ز', 'ع', 'ف', 'ر', 'ا', 'ن'),
            targetWords = listOf("زعفران", "عرفان", "عارف", "فرزان", "فراز"),
            bonusWords = listOf("فاز", "راز", "زار", "نفر", "رعنا")
        ),

        // شهر ۷: جزیره کیش - مروارید نیلگون خلیج فارس
        GameLevel(
            id = 31,
            city = "kish",
            cityNameFa = "کیش - اسکله تفریحی",
            stageNumber = 31,
            letters = listOf('م', 'و', 'ج'),
            targetWords = listOf("موج", "جو"),
            bonusWords = listOf("مو", "جم")
        ),
        GameLevel(
            id = 32,
            city = "kish",
            cityNameFa = "کیش - ساحل مرجان",
            stageNumber = 32,
            letters = listOf('ص', 'د', 'ف'),
            targetWords = listOf("صدف", "صد", "دف", "صف"),
            bonusWords = listOf("فصد")
        ),
        GameLevel(
            id = 33,
            city = "kish",
            cityNameFa = "کیش - شهر زیرزمینی کاریز",
            stageNumber = 33,
            letters = listOf('م', 'ر', 'ج', 'ا', 'ن'),
            targetWords = listOf("مرجان", "رمان", "جام", "نام"),
            bonusWords = listOf("نجار", "مار", "جرم", "ران")
        ),
        GameLevel(
            id = 34,
            city = "kish",
            cityNameFa = "کیش - کشتی یونانی",
            stageNumber = 34,
            letters = listOf('غ', 'و', 'ا', 'ص', 'ر'),
            targetWords = listOf("غواص", "غار", "غوص", "صور"),
            bonusWords = listOf("رو", "او")
        ),
        GameLevel(
            id = 35,
            city = "kish",
            cityNameFa = "کیش - قصر الماس خلیج فارس",
            stageNumber = 35,
            letters = listOf('م', 'ر', 'و', 'ا', 'ر', 'ی', 'د'),
            targetWords = listOf("مروارید", "دیوار", "دارو", "امید", "یار"),
            bonusWords = listOf("رویا", "مردار", "دوام", "مور")
        )
    )

    // 150 cities × 20 stages = 3000 levels, with the handcrafted ones preserved.
    val levels: List<GameLevel> = LevelCatalog.build(handcraftedLevels)

    /** Levels grouped by city key, used by the map, the story milestones and the chests. */
    val levelsByCity: Map<String, List<GameLevel>> = levels.groupBy { it.city }

    // ضرب‌المثل‌ها در فایل‌های دادهٔ جداگانه نگهداری می‌شوند تا کاتالوگ به‌سادگی گسترش یابد.
    val proverbs: List<ProverbLevel> = ProverbCatalog.proverbs

    // Cafe Bazaar Store Monetization Packages (بسته‌های اقتصادی بازار با قیمت‌های جذاب)
    val shopProducts: List<ShopProduct> = listOf(
        ShopProduct(
            id = "bazaar_coins_starter",
            sku = "pkg_starter_coins_500",
            titleFa = "بسته نوپا",
            descriptionFa = "۵۰۰ سکه طلایی برای شروع ماجراجویی و راهنمایی‌ها",
            priceToman = 35000,
            coins = 500,
            gems = 5,
            badgeText = "شروع سریع"
        ),
        ShopProduct(
            id = "bazaar_coins_pro",
            sku = "pkg_pro_coins_1500",
            titleFa = "کیسه نقره‌ای سخن‌دان",
            descriptionFa = "۱,۵۰۰ سکه + ۱۵ الماس با ارزش",
            priceToman = 80000,
            coins = 1500,
            gems = 15,
            badgeText = "محبوب‌ترین"
        ),
        ShopProduct(
            id = "bazaar_coins_champion",
            sku = "pkg_champ_coins_4000",
            titleFa = "صندوقچه طلایی بازار",
            descriptionFa = "۴,۰۰۰ سکه + ۴۰ الماس + ۵ راهنمای رایگان",
            priceToman = 160000,
            coins = 4000,
            gems = 40,
            isBestValue = true,
            badgeText = "تخفیف شگفت‌انگیز ۴۰٪"
        ),
        ShopProduct(
            id = "bazaar_royal_box",
            sku = "pkg_royal_box_10000",
            titleFa = "خزانه سلطنتی قندپارسی",
            descriptionFa = "۱۰,۰۰۰ سکه + ۱۰۰ الماس + حذف دائمی تبلیغات",
            priceToman = 450000,
            coins = 10000,
            gems = 100,
            badgeText = "ویژه ثروتمندان"
        ),
        ShopProduct(
            id = "bazaar_vip_monthly",
            sku = "sub_vip_pass_monthly",
            titleFa = "اشتراک طلایی VIP (یک‌ماهه)",
            descriptionFa = "هر روز ۱۰۰ سکه هدیه + چرخش رایگان گردونه + بدون تبلیغ",
            priceToman = 300000,
            coins = 2000,
            gems = 30,
            isVip = true,
            badgeText = "عضویت ویژه"
        )
    )

    // صرافی قند پارسی: الماس‌ها را به سکه تبدیل کنید
    val gemExchangeOffers: List<GemExchangeOffer> = listOf(
        GemExchangeOffer(
            id = "gem_exchange_small",
            gemCost = 5,
            coinReward = 200,
            titleFa = "کیسه کوچک"
        ),
        GemExchangeOffer(
            id = "gem_exchange_medium",
            gemCost = 10,
            coinReward = 500,
            titleFa = "کیسه زرین",
            isBestValue = true
        ),
        GemExchangeOffer(
            id = "gem_exchange_large",
            gemCost = 25,
            coinReward = 1500,
            titleFa = "خزانه شاهی"
        )
    )

    // 12-Segment Lucky Spin Wheel Rewards. The wheel is intentionally weighted: empty
    // ("پوچ") and small outcomes dominate, while coins and gems are rare. A normal spin is
    // only available as the once-per-day free spin or after watching an ad / spending gems.
    val wheelRewards: List<WheelReward> = listOf(
        WheelReward(
            id = 1,
            labelFa = "پوچ",
            coins = 0,
            colorHex = 0xFFB0BEC5,
            iconEmoji = "🫥",
            weight = 10
        ),
        WheelReward(
            id = 2,
            labelFa = "۱ راهنما حرف",
            coins = 0,
            gems = 0,
            freeLetterHints = 1,
            colorHex = 0xFF3949AB,
            iconEmoji = "💡",
            weight = 4
        ),
        WheelReward(
            id = 3,
            labelFa = "پوچ",
            coins = 0,
            colorHex = 0xFFB0BEC5,
            iconEmoji = "❌",
            weight = 10
        ),
        WheelReward(
            id = 4,
            labelFa = "۱۰ سکه",
            coins = 10,
            colorHex = 0xFF00796B,
            iconEmoji = "🪙",
            weight = 5
        ),
        WheelReward(
            id = 5,
            labelFa = "پوچ",
            coins = 0,
            colorHex = 0xFFB0BEC5,
            iconEmoji = "🎳",
            weight = 10
        ),
        WheelReward(
            id = 6,
            labelFa = "۲۰ سکه",
            coins = 20,
            colorHex = 0xFF00838F,
            iconEmoji = "🪙",
            weight = 4
        ),
        WheelReward(
            id = 7,
            labelFa = "۱ الماس",
            coins = 0,
            gems = 1,
            colorHex = 0xFF1565C0,
            iconEmoji = "💎",
            weight = 2
        ),
        WheelReward(
            id = 8,
            labelFa = "پوچ",
            coins = 0,
            colorHex = 0xFFB0BEC5,
            iconEmoji = "🌵",
            weight = 10
        ),
        WheelReward(
            id = 9,
            labelFa = "۳۰ سکه",
            coins = 30,
            colorHex = 0xFFC2185B,
            iconEmoji = "🏆",
            weight = 3
        ),
        WheelReward(
            id = 10,
            labelFa = "۲ راهنما حرف",
            coins = 0,
            freeLetterHints = 2,
            colorHex = 0xFFD84315,
            iconEmoji = "✨",
            weight = 2
        ),
        WheelReward(
            id = 11,
            labelFa = "پوچ",
            coins = 0,
            colorHex = 0xFFB0BEC5,
            iconEmoji = "🕳️",
            weight = 10
        ),
        WheelReward(
            id = 12,
            labelFa = "جک‌پات ۱۵۰",
            coins = 150,
            gems = 2,
            colorHex = 0xFFFFA000,
            iconEmoji = "👑",
            isJackpot = true,
            weight = 1
        )
    )

    // Simulated National Leaderboard
    val leaderboardUsers: List<LeaderboardUser> = listOf(
        LeaderboardUser(1, "سهراب سپهری‌پور", "تهران", 14250, 0xFFFFB300),
        LeaderboardUser(2, "نگار شیرازی", "فارس", 13800, 0xFF00897B),
        LeaderboardUser(3, "امیررضا نقش‌جهان", "اصفهان", 12450, 0xFF3949AB),
        LeaderboardUser(4, "مریم یزدانی", "یزد", 11900, 0xFFE91E63),
        LeaderboardUser(5, "آرش گیلانی", "گیلان", 10600, 0xFF00ACC1),
        LeaderboardUser(6, "فرزاد خراسانی", "خراسان رضوی", 9850, 0xFF5E35B1),
        LeaderboardUser(7, "شادی کیش‌وندی", "هرمزگان", 8900, 0xFFF4511E),
        LeaderboardUser(8, "بابک تبریزی", "آذربایجان شرقی", 8200, 0xFF7CB342),
        LeaderboardUser(9, "شما (قهرمان پارسی)", "ایران", 6500, 0xFFFF9800, isCurrentPlayer = true),
        LeaderboardUser(10, "پریسا همدانی", "همدان", 6200, 0xFF8D6E63)
    )

    // Daily Challenge. The date is computed at runtime; the challenge itself rotates through a
    // pool of 30 different challenges based on the day of the year.
    val dailyChallenges: List<DailyChallenge> = listOf(
        DailyChallenge("", "چالش دانشمندان", listOf('س', 'ف', 'ی', 'ر', 'ا', 'ن'), listOf("سفیر", "فارس", "نفس", "سیر", "سفر"), 250, 5),
        DailyChallenge("", "چالش بازار", listOf('ب', 'ا', 'ز', 'ر', 'ا'), listOf("بازار", "باز", "راز", "زار", "بر"), 250, 5),
        DailyChallenge("", "چالش دریا", listOf('م', 'و', 'ج', 'د', 'ر', 'ی', 'ا'), listOf("موج", "دریا", "جاری", "مرد", "دور", "مار"), 300, 6),
        DailyChallenge("", "چالش خورشید", listOf('ش', 'م', 'س', 'ت', 'ا'), listOf("شمس", "مست", "اسم", "تم"), 250, 5),
        DailyChallenge("", "چالش قلم", listOf('ق', 'ل', 'م', 'ه'), listOf("قلم", "قلمه", "هم", "مه"), 200, 4),
        DailyChallenge("", "چالش دستان", listOf('د', 'س', 'ت', 'ا', 'ن'), listOf("دست", "است", "دان", "تن", "سن"), 250, 5),
        DailyChallenge("", "چالش کتاب", listOf('ک', 'ت', 'ا', 'ب'), listOf("کتاب", "تاب", "با", "تک", "کت"), 200, 4),
        DailyChallenge("", "چالش سردی", listOf('س', 'ر', 'د', 'ی', 'ا'), listOf("سرد", "دریا", "دیر", "راد", "سرای"), 250, 5),
        DailyChallenge("", "چالش نوش", listOf('ن', 'و', 'ش', 'ا', 'د'), listOf("نوش", "شاد", "ندا"), 200, 4),
        DailyChallenge("", "چالش برف", listOf('ب', 'ر', 'ف', 'ا', 'ن'), listOf("برف", "ناف", "بان", "فر", "فنا"), 250, 5),
        DailyChallenge("", "چالش گلستان", listOf('گ', 'ل', 'ه', 'س', 'ت', 'ن'), listOf("گل", "نسل", "سنگ", "لگن", "تن"), 250, 5),
        DailyChallenge("", "چالش صبح", listOf('ص', 'ب', 'ح', 'ا', 'ن'), listOf("صبح", "حب", "بنا", "ناب"), 200, 4),
        DailyChallenge("", "چالش روزنامه", listOf('ر', 'و', 'ز', 'ن', 'ا', 'م', 'ه'), listOf("روز", "نامه", "نور", "زن", "او"), 300, 6),
        DailyChallenge("", "چالش چرخ", listOf('چ', 'ر', 'خ', 'ا', 'ن'), listOf("چرخ", "خان", "رخ", "نرخ"), 250, 5),
        DailyChallenge("", "چالش سبز", listOf('س', 'ب', 'ز', 'ت', 'ر', 'ا'), listOf("سبز", "تر", "بس", "رز", "بت"), 250, 5),
        DailyChallenge("", "چالش مهر", listOf('م', 'ه', 'ر', 'ب', 'ا', 'ن'), listOf("مهر", "مهربان", "نرم", "منار", "ابر"), 300, 6),
        DailyChallenge("", "چالش پیام", listOf('پ', 'ی', 'ا', 'م', 'گ', 'ر'), listOf("پیام", "گرم", "گام", "مگر", "یار"), 250, 5),
        DailyChallenge("", "چالش خورشید", listOf('خ', 'و', 'ر', 'ش', 'ی', 'د'), listOf("خورشید", "خوش", "شور", "دور", "دیر"), 300, 6),
        DailyChallenge("", "چالش سفر", listOf('س', 'ف', 'ر', 'ک', 'ا', 'ر'), listOf("سفر", "کار", "فکر", "راس"), 250, 5),
        DailyChallenge("", "چالش دریا", listOf('د', 'ر', 'ی', 'ا', 'گ', 'ر'), listOf("دریا", "گرد", "یار", "ارگ"), 250, 5),
        DailyChallenge("", "چالش بیدار", listOf('ب', 'ی', 'د', 'ا', 'ر'), listOf("بیدار", "بید", "دار", "یاد", "در"), 250, 5),
        DailyChallenge("", "چالش کوچه", listOf('ک', 'و', 'چ', 'ه', 'ا'), listOf("کوچه", "چه", "که", "اکو"), 200, 4),
        DailyChallenge("", "چالش منزل", listOf('م', 'ن', 'ز', 'ل', 'گ', 'ا', 'ه'), listOf("منزل", "گاه", "نگاه", "گمان", "زنگ"), 300, 6),
        DailyChallenge("", "چالش نمک", listOf('ن', 'م', 'ک', 'د', 'ا', 'ن'), listOf("نمک", "دکان", "کمان", "مکان"), 250, 5),
        DailyChallenge("", "چالش مرد", listOf('م', 'ر', 'د', 'ا', 'ن'), listOf("مرد", "نار", "راد", "دان", "ما"), 250, 5),
        DailyChallenge("", "چالش شبنم", listOf('ش', 'ب', 'ن', 'م', 'ز', 'ا'), listOf("شبنم", "شام", "ناب", "زن", "بام"), 300, 6),
        DailyChallenge("", "چالش قرار", listOf('ق', 'ر', 'ا', 'ر', 'گ', 'ا', 'ه'), listOf("قرار", "گاه", "قهر", "هر"), 250, 5),
        DailyChallenge("", "چالش آموزش", listOf('ا', 'م', 'و', 'ز', 'ش', 'گ', 'ا', 'ه'), listOf("آموز", "آموزش", "گاه", "شام", "گاز"), 300, 6),
        DailyChallenge("", "چالش خودکار", listOf('خ', 'و', 'د', 'ک', 'ا', 'ر'), listOf("خود", "کار", "خور", "دار", "او"), 250, 5),
        DailyChallenge("", "چالش سازمان", listOf('س', 'ا', 'ز', 'م', 'ا', 'ن'), listOf("ساز", "سازمان", "نام", "مان", "سان"), 300, 6)
    )

    fun todayChallenge(): DailyChallenge {
        val calendar = java.util.Calendar.getInstance()
        val dayOfYear = calendar.get(java.util.Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1) % dailyChallenges.size
        val base = dailyChallenges[index]
        return base.copy(
            dateString = com.aistudio.qandparsi.util.PersianDate.today(),
            letters = scrambleLetters(base.letters, base.targetWords, dayOfYear)
        )
    }

    /**
     * The letter banks used to be stored in the exact spelling order of the answer words
     * (e.g. سفیر → س,ف,ی,ر,...), which gave the solution away. This guarantees a shuffled
     * order that never contains any target word as a contiguous run of letters.
     */
    fun scrambleLetters(letters: List<Char>, targetWords: List<String>, seed: Int): List<Char> =
        LetterScrambler.scramble(letters, targetWords, seed)

    // 7-Day 24-Hour Daily Login Bonus Rewards (پاداش ورود ۲۴ ساعته)
    val dailyLoginRewards: List<com.aistudio.qandparsi.data.model.DailyLoginReward> = listOf(
        com.aistudio.qandparsi.data.model.DailyLoginReward(
            dayNumber = 1,
            dayTitleFa = "روز اول",
            coins = 80,
            gems = 0,
            freeLetterHints = 1,
            iconEmoji = "🪙"
        ),
        com.aistudio.qandparsi.data.model.DailyLoginReward(
            dayNumber = 2,
            dayTitleFa = "روز دوم",
            coins = 120,
            gems = 1,
            freeLetterHints = 0,
            iconEmoji = "💎"
        ),
        com.aistudio.qandparsi.data.model.DailyLoginReward(
            dayNumber = 3,
            dayTitleFa = "روز سوم",
            coins = 180,
            gems = 1,
            freeLetterHints = 2,
            iconEmoji = "💡"
        ),
        com.aistudio.qandparsi.data.model.DailyLoginReward(
            dayNumber = 4,
            dayTitleFa = "روز چهارم",
            coins = 250,
            gems = 2,
            freeLetterHints = 1,
            iconEmoji = "💰"
        ),
        com.aistudio.qandparsi.data.model.DailyLoginReward(
            dayNumber = 5,
            dayTitleFa = "روز پنجم",
            coins = 350,
            gems = 2,
            freeWordHints = 1,
            iconEmoji = "🔮"
        ),
        com.aistudio.qandparsi.data.model.DailyLoginReward(
            dayNumber = 6,
            dayTitleFa = "روز ششم",
            coins = 450,
            gems = 3,
            freeLetterHints = 2,
            iconEmoji = "✨"
        ),
        com.aistudio.qandparsi.data.model.DailyLoginReward(
            dayNumber = 7,
            dayTitleFa = "روز هفتم",
            coins = 700,
            gems = 6,
            freeLetterHints = 3,
            freeWordHints = 1,
            isGrandPrize = true,
            iconEmoji = "👑"
        )
    )
}
