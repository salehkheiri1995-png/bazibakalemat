package com.aistudio.qandparsi.data.repository

/** One reusable word set: the letters are a superset of every target and bonus word. */
data class LevelTemplate(
    val letters: List<Char>,
    val targetWords: List<String>,
    val bonusWords: List<String>
)

/**
 * Twenty hand-verified word sets ordered from easy to hard. Every city gets its own copy of
 * the ramp, so the 150-city map stays solvable while difficulty still grows stage by stage.
 * `GameDataValidationTest` proves that all words can be formed from the letters.
 */
object LevelTemplates {

    val stageTitles: List<String> = listOf(
        "دروازهٔ شهر",
        "بازار کهنه",
        "چای‌خانهٔ سنتی",
        "کوچه‌باغ‌ها",
        "مسجد جامع",
        "قلعهٔ تاریخی",
        "خانهٔ قدیمی",
        "پل کهن",
        "کاروانسرا",
        "موزهٔ شهر",
        "تپهٔ باستانی",
        "باغ ایرانی",
        "حمام تاریخی",
        "برج دیده‌بانی",
        "بازار مسگرها",
        "میدان مرکزی",
        "محلهٔ هنرمندان",
        "دریاچهٔ کوچک",
        "آسیاب آبی",
        "برج یادمان"
    )

    val templates: List<LevelTemplate> = listOf(
        LevelTemplate(listOf('ه', 'ن', 'ر'), listOf("هنر", "نهر", "رهن"), listOf("هر", "نه")),
        LevelTemplate(listOf('ق', 'ل', 'م', 'ه'), listOf("قلم", "قلمه", "هم"), listOf("مه")),
        LevelTemplate(
            listOf('س', 'ل', 'ا', 'م'),
            listOf("سلام", "مال", "لمس"),
            listOf("سالم", "مس", "سم")
        ),
        LevelTemplate(
            listOf('ن', 'ب', 'ا', 'ت'),
            listOf("نبات", "تاب", "ناب", "تن"),
            listOf("تب", "بت", "بنا")
        ),
        LevelTemplate(listOf('پ', 'ی', 'ا', 'ز'), listOf("پیاز", "زیپ", "پا"), listOf("یا", "پز")),
        LevelTemplate(listOf('ک', 'ش', 'ت', 'ی'), listOf("کشتی", "شک"), listOf("یک", "کی")),
        LevelTemplate(
            listOf('ب', 'ه', 'ا', 'ر'),
            listOf("بهار", "راه", "ابر", "بار"),
            listOf("بره", "هر", "به")
        ),
        LevelTemplate(
            listOf('ک', 'ا', 'ر', 'د'),
            listOf("کارد", "اردک", "کادر", "درک"),
            listOf("راکد", "کدر", "رک")
        ),
        LevelTemplate(
            listOf('ب', 'س', 'ت', 'ه', 'د'),
            listOf("بسته", "دست", "سبد"),
            listOf("بست", "دبه")
        ),
        LevelTemplate(
            listOf('م', 'ا', 'ه', 'ی', 'د'),
            listOf("ماهی", "ماه", "دام"),
            listOf("هما", "دما")
        ),
        LevelTemplate(listOf('ق', 'ن', 'د', 'و'), listOf("قند", "نقد", "نو"), listOf("دو", "قو")),
        LevelTemplate(listOf('گ', 'ل', 'ا', 'ب', 'ر'), listOf("گلاب", "برگ", "بار"), listOf("رگ", "لب")),
        LevelTemplate(
            listOf('خ', 'ا', 'ن', 'ه', 'د', 'ن'),
            listOf("خانه", "خنده", "ناخن", "دانه"),
            listOf("نان", "خدا")
        ),
        LevelTemplate(
            listOf('س', 'ف', 'ر', 'ا', 'ه'),
            listOf("سفر", "سفره", "سرفه"),
            listOf("راه")
        ),
        LevelTemplate(
            listOf('د', 'ر', 'ی', 'ا', 'ب'),
            listOf("دریا", "یار", "دیر", "بار"),
            listOf("بید", "دار")
        ),
        LevelTemplate(
            listOf('ش', 'ک', 'ر', 'ا', 'ن'),
            listOf("شکر", "شکار", "کنار", "کار"),
            listOf("کاش", "رشک", "کان")
        ),
        LevelTemplate(
            listOf('ب', 'ا', 'ز', 'ا', 'ر'),
            listOf("بازار", "ابزار", "بارز", "بار"),
            listOf("راز", "باز", "زار")
        ),
        LevelTemplate(
            listOf('د', 'ا', 'ن', 'ش', 'گ', 'ن'),
            listOf("دانش", "نشان", "شاد"),
            listOf("گشاد")
        ),
        LevelTemplate(
            listOf('م', 'ه', 'ر', 'ب', 'ا', 'ن'),
            listOf("مهربان", "بهار", "نامه"),
            listOf("نرم", "بهمن", "ماهر")
        ),
        LevelTemplate(
            listOf('ف', 'ر', 'ه', 'ن', 'گ'),
            listOf("فرهنگ", "هنر", "فرنگ"),
            listOf("نگه", "هر")
        )
    )
}
