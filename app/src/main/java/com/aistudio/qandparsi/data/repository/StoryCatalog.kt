package com.aistudio.qandparsi.data.repository

import com.aistudio.qandparsi.data.model.DiaryPage
import com.aistudio.qandparsi.data.model.GameLevel
import com.aistudio.qandparsi.data.model.StoryUnlock

/**
 * «راز طومار هفت اقلیم» — the story that ties the map together.
 *
 * Hatif, a young calligrapher, is rebuilding the lost divan of «قند پارسی». Every five
 * stages of a city he finds a leaf of the scroll and an ancient souvenir; finishing a city
 * earns its golden plaque. Texts are composed from the city/province data so all 150 cities
 * get their own consistent narrative without thousands of hand-written strings.
 */
object StoryCatalog {

    private val souvenirs: List<Pair<String, String>> = listOf(
        "گلیم دست‌باف" to "🧶",
        "میناکاری" to "🎨",
        "فیروزهٔ نیشابور" to "💠",
        "زعفران" to "🌺",
        "خاتم‌کاری" to "🪵",
        "سفال لعاب‌دار" to "🏺",
        "چای بهاره" to "🍃",
        "شیرینی سنتی" to "🍮",
        "گلاب قمصر" to "🌹",
        "ترمهٔ یزد" to "🧵",
        "مسگری اصفهان" to "🔔",
        "عسل کوهستان" to "🍯",
        "قالیچهٔ دست‌باف" to "🧷",
        "قلم‌زنی" to "🖋️",
        "شیشه‌گری" to "🫙"
    )

    /** The diary page unlocked by [level], or null when the stage is not a milestone. */
    fun pageFor(level: GameLevel): DiaryPage? {
        val milestone = level.cityStageNumber
        if (milestone <= 0 || milestone % 5 != 0) return null
        val city = CityCatalog.cityFor(level.city) ?: return null
        val cityIndex = CityCatalog.indexOf(level.city)
        val souvenir = souvenirs[(cityIndex + milestone) % souvenirs.size]
        val chapter = milestone / 5
        val title = when (chapter) {
            1 -> "برگ نخست طومار"
            2 -> "برگ دوم طومار"
            3 -> "برگ سوم طومار"
            else -> "برگ واپسین طومار"
        }
        return DiaryPage(
            cityKey = city.key,
            cityNameFa = city.nameFa,
            milestone = milestone,
            title = title,
            body = bodyFor(chapter, city.nameFa, city.provinceFa),
            souvenirName = souvenir.first,
            souvenirEmoji = souvenir.second
        )
    }

    /** A collectible souvenir awarded by the secret chest of [cityKey]. */
    fun souvenirFor(cityKey: String): Pair<String, String> {
        val cityIndex = CityCatalog.indexOf(cityKey).coerceAtLeast(0)
        return souvenirs[(cityIndex * 3 + 1) % souvenirs.size]
    }

    /** The golden plaque text awarded for finishing a city, or null for regular stages. */
    fun plaqueFor(level: GameLevel): String? {
        if (level.cityStageNumber != level.cityStageCount) return null
        val city = CityCatalog.cityFor(level.city) ?: return null
        return "لوح طلایی ${city.nameFa} در تالار افتخارات نصب شد"
    }

    private fun bodyFor(chapter: Int, cityName: String, province: String): String = when (chapter) {
        1 ->
            "هاتف قلم را در دوات زد و در دروازهٔ $cityName ایستاد. «هر شهر، یک برگ از دیوان " +
                "گمشده را پنهان کرده است.» نخستین واژه‌ها را که یافت، بوی کاغذ کهنه و " +
                "مرکب آویشن گرفت و دانست که در $province بر مدار درست ایستاده است."
        2 ->
            "در کوچه‌های $cityName، پیرمردی نقره‌کار به هاتف گفت: «طومار را نمی‌شود یک‌جا یافت؛ " +
                "هر پنج گام، حرفی از آن آشکار می‌شود.» هاتف واژه‌های تازه را در دفتر نوشت و " +
                "در حاشیهٔ صفحه، نام $province را با خط ثلث ثبت کرد."
        3 ->
            "شب در $cityName بود که چراغی در پنجرهٔ کاروانسرا روشن ماند. هاتف از میان واژه‌ها " +
                "بیتی به یادگار یافت که در هیچ نسخه‌ای نبود. بوی چای و نان تازه، خستگی " +
                "$province را از تنش شست."
        else ->
            "آخرین برگ طومار در $cityName به دست آمد. هاتف خط را با دستی لرزان تمام کرد و " +
                "دانست که اقلیم $province دیگر در حافظهٔ او زنده است. نام شهر را بر لوحی " +
                "طلایی نوشت تا یادگار بماند."
    }

    fun unlockFor(level: GameLevel): StoryUnlock? {
        val page = pageFor(level) ?: return null
        val plaque = plaqueFor(level)
        return StoryUnlock(page = page, cityCompleted = plaque != null, plaqueText = plaque)
    }
}
