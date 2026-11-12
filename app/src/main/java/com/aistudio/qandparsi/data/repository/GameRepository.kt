package com.aistudio.qandparsi.data.repository

import com.aistudio.qandparsi.data.model.DailyChallenge
import com.aistudio.qandparsi.data.model.GameLevel
import com.aistudio.qandparsi.data.model.LeaderboardUser
import com.aistudio.qandparsi.data.model.ProverbLevel
import com.aistudio.qandparsi.data.model.ShopProduct
import com.aistudio.qandparsi.data.model.WheelReward

object GameRepository {

    // 35 Handcrafted Levels spanning 7 major Iranian tourist/cultural hubs
    val levels: List<GameLevel> = listOf(
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

    // 10 Persian Proverb Levels with thoroughly scrambled letter banks
    val proverbs: List<ProverbLevel> = listOf(
        ProverbLevel(
            id = 1,
            proverbFull = "آشپز که دو تا شد آش یا شور میشه یا بی‌نمک",
            maskedProverb = "... که دو تا شد آش یا شور میشه یا بی‌نمک",
            missingWord = "آشپز",
            meaning = "وقتی در کاری چند نفر بدون هماهنگی دخالت کنند، کار خراب می‌شود.",
            emojiClue = "👨‍🍳🍲🧂",
            scrambledLetters = listOf('پ', 'م', 'ز', 'د', 'ش', 'ک', 'آ', 'ت', 'ر', 'ب')
        ),
        ProverbLevel(
            id = 2,
            proverbFull = "با یک گل بهار نمیشه",
            maskedProverb = "با یک گل ... نمیشه",
            missingWord = "بهار",
            meaning = "یک نشانه کوچک برای رخ دادن یک اتفاق بزرگ کافی نیست و تلاش مداوم لازم است.",
            emojiClue = "🌸🌱☀️",
            scrambledLetters = listOf('ه', 'ر', 'س', 'ب', 'م', 'ا', 'ک', 'ت', 'د', 'ل')
        ),
        ProverbLevel(
            id = 3,
            proverbFull = "قطره قطره جمع گردد وانگهی دریا شود",
            maskedProverb = "قطره قطره جمع گردد وانگهی ... شود",
            missingWord = "دریا",
            meaning = "پس‌انداز و تلاش‌های اندک اندک در طول زمان به نتایج بزرگی منجر می‌شود.",
            emojiClue = "💧🌊✨",
            scrambledLetters = listOf('ی', 'س', 'د', 'م', 'ر', 'ک', 'ا', 'ت', 'ن', 'ب')
        ),
        ProverbLevel(
            id = 4,
            proverbFull = "جوجه رو آخر پاییز می‌شمارند",
            maskedProverb = "جوجه رو آخر ... می‌شمارند",
            missingWord = "پاییز",
            meaning = "نباید قبل از اتمام کار و دیدن نتیجه نهایی، مغرور شد یا قضاوت کرد.",
            emojiClue = "🐣🍂🍁",
            scrambledLetters = listOf('ی', 'ب', 'ا', 'ز', 'ه', 'پ', 'ر', 'ی', 'س', 'ت')
        ),
        ProverbLevel(
            id = 5,
            proverbFull = "دست بالای دست بسیار است",
            maskedProverb = "دست بالای ... بسیار است",
            missingWord = "دست",
            meaning = "هر چقدر هم در کاری توانا باشی، ممکن است کسی از تو ماهرتر پیدا شود؛ پس باید فروتن بود.",
            emojiClue = "✋🌟👑",
            scrambledLetters = listOf('س', 'م', 'ت', 'ب', 'د', 'ر', 'ک', 'ن', 'ل', 'پ')
        ),
        ProverbLevel(
            id = 6,
            proverbFull = "شتر دیدی ندیدی",
            maskedProverb = "شتر دیدی ...",
            missingWord = "ندیدی",
            meaning = "حفظ اسرار و خودداری از دخالت و سخن‌چینی در مسائل دیگران.",
            emojiClue = "🐫🙈🤐",
            scrambledLetters = listOf('د', 'ک', 'ی', 'م', 'ن', 'ب', 'د', 'س', 'ی', 'ت')
        ),
        ProverbLevel(
            id = 7,
            proverbFull = "کوه به کوه نمیرسه آدم به آدم میرسه",
            maskedProverb = "کوه به کوه نمیرسه ... به آدم میرسه",
            missingWord = "آدم",
            meaning = "انسان‌ها هر چقدر هم از هم دور باشند روزی دوباره گذارشان به هم خواهد افتاد.",
            emojiClue = "⛰️🚶‍♂️🤝",
            scrambledLetters = listOf('د', 'س', 'آ', 'ر', 'م', 'ت', 'ن', 'ب', 'ک', 'ل')
        ),
        ProverbLevel(
            id = 8,
            proverbFull = "مارگزیده از ریسمان سیاه و سفید می‌ترسه",
            maskedProverb = "مارگزیده از ... سیاه و سفید می‌ترسه",
            missingWord = "ریسمان",
            meaning = "کسی که از چیزی ضربه خورده حتی از شبیه آن هم احتیاط می‌کند.",
            emojiClue = "🐍🧵😨",
            scrambledLetters = listOf('س', 'ت', 'ر', 'ک', 'ی', 'ب', 'م', 'ن', 'ا', 'د')
        ),
        ProverbLevel(
            id = 9,
            proverbFull = "فلفل نبین چه ریزه بشکن ببین چه تیزه",
            maskedProverb = "فلفل نبین چه ریزه بشکن ببین چه ...",
            missingWord = "تیزه",
            meaning = "نباید افراد را از روی جثه یا ظاهر کوچک قضاوت کرد.",
            emojiClue = "🌶️🔥⚡",
            scrambledLetters = listOf('ی', 'ب', 'ت', 'س', 'ز', 'ک', 'ه', 'م', 'ن', 'ر')
        ),
        ProverbLevel(
            id = 10,
            proverbFull = "ماهی رو هر وقت از آب بگیری تازه است",
            maskedProverb = "ماهی رو هر وقت از آب بگیری ... است",
            missingWord = "تازه",
            meaning = "جلوگیری از ضرر و شروع کار درست در هر زمانی پسندیده و مغتنم است.",
            emojiClue = "🐟🌊🎣",
            scrambledLetters = listOf('ا', 'ک', 'ت', 'م', 'ز', 'س', 'ه', 'ب', 'ر', 'د')
        )
    )

    // Cafe Bazaar Store Monetization Packages (بسته‌های اقتصادی بازار با قیمت‌های جذاب)
    val shopProducts: List<ShopProduct> = listOf(
        ShopProduct(
            id = "bazaar_coins_starter",
            sku = "pkg_starter_coins_500",
            titleFa = "بسته نوپا",
            descriptionFa = "۵۰۰ سکه طلایی برای شروع ماجراجویی و راهنمایی‌ها",
            priceToman = 25000,
            coins = 500,
            gems = 5,
            badgeText = "شروع سریع"
        ),
        ShopProduct(
            id = "bazaar_coins_pro",
            sku = "pkg_pro_coins_1500",
            titleFa = "کیسه نقره‌ای سخن‌دان",
            descriptionFa = "۱,۵۰۰ سکه + ۱۵ الماس با ارزش",
            priceToman = 65000,
            coins = 1500,
            gems = 15,
            badgeText = "محبوب‌ترین"
        ),
        ShopProduct(
            id = "bazaar_coins_champion",
            sku = "pkg_champ_coins_4000",
            titleFa = "صندوقچه طلایی بازار",
            descriptionFa = "۴,۰۰۰ سکه + ۴۰ الماس + ۵ راهنمای رایگان",
            priceToman = 149000,
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
            priceToman = 299000,
            coins = 10000,
            gems = 100,
            badgeText = "ویژه ثروتمندان"
        ),
        ShopProduct(
            id = "bazaar_vip_monthly",
            sku = "sub_vip_pass_monthly",
            titleFa = "اشتراک طلایی VIP (یک‌ماهه)",
            descriptionFa = "هر روز ۱۰۰ سکه هدیه + چرخش رایگان گردونه + بدون تبلیغ",
            priceToman = 199000,
            coins = 2000,
            gems = 30,
            isVip = true,
            badgeText = "عضویت ویژه"
        )
    )

    // 8-Segment Lucky Spin Wheel Rewards (گردونه شانس با جوایز سکه و راهنماهای کاربردی)
    val wheelRewards: List<WheelReward> = listOf(
        WheelReward(
            id = 1,
            labelFa = "۱۰۰ سکه",
            coins = 100,
            gems = 0,
            colorHex = 0xFF00796B,
            iconEmoji = "🪙"
        ),
        WheelReward(
            id = 2,
            labelFa = "۱ راهنما حرف",
            coins = 0,
            gems = 0,
            freeLetterHints = 1,
            colorHex = 0xFF3949AB,
            iconEmoji = "💡"
        ),
        WheelReward(
            id = 3,
            labelFa = "۲۰۰ سکه",
            coins = 200,
            gems = 1,
            colorHex = 0xFFC2185B,
            iconEmoji = "💰"
        ),
        WheelReward(
            id = 4,
            labelFa = "جک‌پات ۸۰۰",
            coins = 800,
            gems = 5,
            freeLetterHints = 2,
            colorHex = 0xFFFFA000,
            iconEmoji = "👑",
            isJackpot = true
        ),
        WheelReward(
            id = 5,
            labelFa = "۱ فاش کلمه",
            coins = 0,
            gems = 1,
            freeWordHints = 1,
            colorHex = 0xFF7B1FA2,
            iconEmoji = "🔮"
        ),
        WheelReward(
            id = 6,
            labelFa = "۳۰۰ سکه",
            coins = 300,
            gems = 0,
            colorHex = 0xFF00838F,
            iconEmoji = "🪙"
        ),
        WheelReward(
            id = 7,
            labelFa = "۲ راهنما حرف",
            coins = 50,
            gems = 0,
            freeLetterHints = 2,
            colorHex = 0xFFD84315,
            iconEmoji = "✨"
        ),
        WheelReward(
            id = 8,
            labelFa = "۵ الماس",
            coins = 150,
            gems = 5,
            colorHex = 0xFF1565C0,
            iconEmoji = "💎"
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
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1) % dailyChallenges.size
        return dailyChallenges[index].copy(dateString = com.aistudio.qandparsi.util.PersianDate.today())
    }

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
