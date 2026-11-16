package com.aistudio.qandparsi.data.repository

/** A playable city on the «راز طومار هفت اقلیم» map. */
data class CityDefinition(
    val key: String,
    val nameFa: String,
    val provinceFa: String,
    val emoji: String = "📍"
)

/**
 * 150 Iranian cities from all 31 provinces. The first seven entries are the cities that
 * historically shipped with hand-written levels, so their content is preserved.
 */
object CityCatalog {

    const val STAGES_PER_CITY = 20

    val cities: List<CityDefinition> = listOf(
        // شهرهای دارای محتوای دست‌نویس (به ترتیب نسخه‌های قبلی)
        CityDefinition("tehran", "تهران", "تهران", "🏙️"),
        CityDefinition("shiraz", "شیراز", "فارس", "🌷"),
        CityDefinition("isfahan", "اصفهان", "اصفهان", "🏛️"),
        CityDefinition("yazd", "یزد", "یزد", "🕌"),
        CityDefinition("gilan", "رشت", "گیلان", "🌾"),
        CityDefinition("mashhad", "مشهد", "خراسان رضوی", "🕋"),
        CityDefinition("kish", "کیش", "هرمزگان", "🏝️"),

        // تهران و البرز
        CityDefinition("eslamshahr", "اسلامشهر", "تهران"),
        CityDefinition("shahriar", "شهریار", "تهران"),
        CityDefinition("varamin", "ورامین", "تهران"),
        CityDefinition("karaj", "کرج", "البرز"),
        CityDefinition("fardis", "فردیس", "البرز"),
        CityDefinition("nazarabad", "نظرآباد", "البرز"),
        CityDefinition("hashtgerd", "هشتگرد", "البرز"),

        // اصفهان
        CityDefinition("kashan", "کاشان", "اصفهان", "🌺"),
        CityDefinition("najafabad", "نجف‌آباد", "اصفهان"),
        CityDefinition("khomeinishahr", "خمینی‌شهر", "اصفهان"),
        CityDefinition("shahreza", "شهرضا", "اصفهان"),
        CityDefinition("golpayegan", "گلپایگان", "اصفهان"),
        CityDefinition("nain", "نائین", "اصفهان"),

        // فارس
        CityDefinition("marvdasht", "مرودشت", "فارس", "🏺"),
        CityDefinition("kazerun", "کازرون", "فارس"),
        CityDefinition("lar", "لار", "فارس"),
        CityDefinition("jahrom", "جهرم", "فارس"),
        CityDefinition("fasa", "فسا", "فارس"),
        CityDefinition("darab", "داراب", "فارس"),

        // خراسان رضوی
        CityDefinition("neyshabur", "نیشابور", "خراسان رضوی", "🌿"),
        CityDefinition("sabzevar", "سبزوار", "خراسان رضوی"),
        CityDefinition("torbat_heidarieh", "تربت حیدریه", "خراسان رضوی"),
        CityDefinition("quchan", "قوچان", "خراسان رضوی"),
        CityDefinition("kashmar", "کاشمر", "خراسان رضوی"),
        CityDefinition("gonabad", "گناباد", "خراسان رضوی"),

        // خراسان جنوبی و شمالی
        CityDefinition("birjand", "بیرجند", "خراسان جنوبی", "🌵"),
        CityDefinition("qaen", "قائن", "خراسان جنوبی"),
        CityDefinition("ferdows", "فردوس", "خراسان جنوبی"),
        CityDefinition("nehbandan", "نهبندان", "خراسان جنوبی"),
        CityDefinition("bojnurd", "بجنورد", "خراسان شمالی"),
        CityDefinition("shirvan", "شیروان", "خراسان شمالی"),
        CityDefinition("esfarayen", "اسفراین", "خراسان شمالی"),
        CityDefinition("jajarm", "جاجرم", "خراسان شمالی"),

        // آذربایجان شرقی و غربی
        CityDefinition("tabriz", "تبریز", "آذربایجان شرقی", "🧵"),
        CityDefinition("maragheh", "مراغه", "آذربایجان شرقی"),
        CityDefinition("marand", "مرند", "آذربایجان شرقی"),
        CityDefinition("mianeh", "میانه", "آذربایجان شرقی"),
        CityDefinition("ahar", "اهر", "آذربایجان شرقی"),
        CityDefinition("sarab", "سراب", "آذربایجان شرقی"),
        CityDefinition("jolfa", "جلفا", "آذربایجان شرقی"),
        CityDefinition("urmia", "ارومیه", "آذربایجان غربی", "🍇"),
        CityDefinition("khoy", "خوی", "آذربایجان غربی"),
        CityDefinition("miandoab", "میاندوآب", "آذربایجان غربی"),
        CityDefinition("bukan", "بوکان", "آذربایجان غربی"),
        CityDefinition("mahabad", "مهاباد", "آذربایجان غربی"),
        CityDefinition("maku", "ماکو", "آذربایجان غربی"),

        // اردبیل و زنجان
        CityDefinition("ardabil", "اردبیل", "اردبیل", "♨️"),
        CityDefinition("khalkhal", "خلخال", "اردبیل"),
        CityDefinition("parsabad", "پارس‌آباد", "اردبیل"),
        CityDefinition("meshgin", "مشگین‌شهر", "اردبیل"),
        CityDefinition("germi", "گرمی", "اردبیل"),
        CityDefinition("zanjan", "زنجان", "زنجان", "🔪"),
        CityDefinition("abhar", "ابهر", "زنجان"),
        CityDefinition("mahneshan", "ماهنشان", "زنجان"),

        // گیلان
        CityDefinition("lahijan", "لاهیجان", "گیلان", "🍃"),
        CityDefinition("anzali", "انزلی", "گیلان", "⛵"),
        CityDefinition("astara", "آستارا", "گیلان"),
        CityDefinition("fuman", "فومن", "گیلان"),
        CityDefinition("masuleh", "ماسوله", "گیلان", "🏘️"),
        CityDefinition("rudsar", "رودسر", "گیلان"),

        // مازندران
        CityDefinition("sari", "ساری", "مازندران", "🌳"),
        CityDefinition("babol", "بابل", "مازندران"),
        CityDefinition("amol", "آمل", "مازندران"),
        CityDefinition("ramsar", "رامسر", "مازندران"),
        CityDefinition("babolsar", "بابلسر", "مازندران"),
        CityDefinition("chalus", "چالوس", "مازندران"),
        CityDefinition("nowshahr", "نوشهر", "مازندران"),

        // گلستان و سمنان
        CityDefinition("gorgan", "گرگان", "گلستان", "🌲"),
        CityDefinition("gonbad", "گنبد کاووس", "گلستان"),
        CityDefinition("bandar_torkaman", "بندر ترکمن", "گلستان"),
        CityDefinition("aliabad", "علی‌آباد", "گلستان"),
        CityDefinition("semnan", "سمنان", "سمنان", "🐪"),
        CityDefinition("shahrud", "شاهرود", "سمنان"),
        CityDefinition("damghan", "دامغان", "سمنان"),
        CityDefinition("garmsar", "گرمسار", "سمنان"),

        // قم و مرکزی
        CityDefinition("qom", "قم", "قم", "📜"),
        CityDefinition("arak", "اراک", "مرکزی", "🏭"),
        CityDefinition("saveh", "ساوه", "مرکزی"),
        CityDefinition("khomein", "خمین", "مرکزی"),
        CityDefinition("mahallat", "محلات", "مرکزی"),

        // همدان و کرمانشاه
        CityDefinition("hamadan", "همدان", "همدان", "🗿"),
        CityDefinition("malayer", "ملایر", "همدان"),
        CityDefinition("nahavand", "نهاوند", "همدان"),
        CityDefinition("tuyserkan", "تویسرکان", "همدان"),
        CityDefinition("asadabad", "اسدآباد", "همدان"),
        CityDefinition("kermanshah", "کرمانشاه", "کرمانشاه", "⛰️"),
        CityDefinition("eslamabad_gharb", "اسلام‌آباد غرب", "کرمانشاه"),
        CityDefinition("paveh", "پاوه", "کرمانشاه"),
        CityDefinition("javanrud", "جوانرود", "کرمانشاه"),
        CityDefinition("kangavar", "کنگاور", "کرمانشاه"),

        // کردستان و لرستان
        CityDefinition("sanandaj", "سنندج", "کردستان", "🎻"),
        CityDefinition("saqqez", "سقز", "کردستان"),
        CityDefinition("marivan", "مریوان", "کردستان"),
        CityDefinition("baneh", "بانه", "کردستان"),
        CityDefinition("bijar", "بیجار", "کردستان"),
        CityDefinition("khorramabad", "خرم‌آباد", "لرستان", "🏞️"),
        CityDefinition("borujerd", "بروجرد", "لرستان"),
        CityDefinition("dorud", "دورود", "لرستان"),
        CityDefinition("aligudarz", "الیگودرز", "لرستان"),
        CityDefinition("kuhdasht", "کوهدشت", "لرستان"),

        // ایلام و خوزستان
        CityDefinition("ilam", "ایلام", "ایلام", "🌄"),
        CityDefinition("dehloran", "دهلران", "ایلام"),
        CityDefinition("abdanan", "آبدانان", "ایلام"),
        CityDefinition("ahvaz", "اهواز", "خوزستان", "🌴"),
        CityDefinition("abadan", "آبادان", "خوزستان"),
        CityDefinition("khorramshahr", "خرمشهر", "خوزستان"),
        CityDefinition("dezful", "دزفول", "خوزستان", "🌉"),
        CityDefinition("shushtar", "شوشتر", "خوزستان"),
        CityDefinition("behbahan", "بهبهان", "خوزستان"),
        CityDefinition("andimeshk", "اندیمشک", "خوزستان"),

        // چهارمحال و کهگیلویه
        CityDefinition("shahrekord", "شهرکرد", "چهارمحال و بختیاری", "❄️"),
        CityDefinition("borujen", "بروجن", "چهارمحال و بختیاری"),
        CityDefinition("farsan", "فارسان", "چهارمحال و بختیاری"),
        CityDefinition("lordegan", "لردگان", "چهارمحال و بختیاری"),
        CityDefinition("yasuj", "یاسوج", "کهگیلویه و بویراحمد", "🌊"),
        CityDefinition("dogonbadan", "دوگنبدان", "کهگیلویه و بویراحمد"),
        CityDefinition("dehdasht", "دهدشت", "کهگیلویه و بویراحمد"),

        // بوشهر و هرمزگان
        CityDefinition("bushehr", "بوشهر", "بوشهر", "🐟"),
        CityDefinition("borazjan", "برازجان", "بوشهر"),
        CityDefinition("genaveh", "گناوه", "بوشهر"),
        CityDefinition("daylam", "دیلم", "بوشهر"),
        CityDefinition("kangan", "کنگان", "بوشهر"),
        CityDefinition("bandarabbas", "بندرعباس", "هرمزگان", "🚢"),
        CityDefinition("qeshm", "قشم", "هرمزگان", "🦀"),
        CityDefinition("minab", "میناب", "هرمزگان"),
        CityDefinition("bandar_lengeh", "بندر لنگه", "هرمزگان"),
        CityDefinition("bastak", "بستک", "هرمزگان"),

        // سیستان و بلوچستان
        CityDefinition("zahedan", "زاهدان", "سیستان و بلوچستان", "🌪️"),
        CityDefinition("chabahar", "چابهار", "سیستان و بلوچستان", "🐬"),
        CityDefinition("iranshahr", "ایرانشهر", "سیستان و بلوچستان"),
        CityDefinition("zabol", "زابل", "سیستان و بلوچستان"),
        CityDefinition("saravan", "سراوان", "سیستان و بلوچستان"),
        CityDefinition("nikshahr", "نیکشهر", "سیستان و بلوچستان"),

        // کرمان و یزد
        CityDefinition("kerman", "کرمان", "کرمان", "🌹"),
        CityDefinition("sirjan", "سیرجان", "کرمان"),
        CityDefinition("rafsanjan", "رفسنجان", "کرمان"),
        CityDefinition("bam", "بم", "کرمان", "🏰"),
        CityDefinition("jiroft", "جیرفت", "کرمان"),
        CityDefinition("bardsir", "بردسیر", "کرمان"),
        CityDefinition("meybod", "میبد", "یزد", "🧶"),
        CityDefinition("ardakan", "اردکان", "یزد"),
        CityDefinition("bafq", "بافق", "یزد"),
        CityDefinition("mehriz", "مهریز", "یزد")
    )

    fun cityFor(key: String): CityDefinition? = cities.firstOrNull { it.key == key }

    fun indexOf(key: String): Int = cities.indexOfFirst { it.key == key }
}
