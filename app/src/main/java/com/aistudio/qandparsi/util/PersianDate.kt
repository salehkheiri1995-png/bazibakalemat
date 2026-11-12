package com.aistudio.qandparsi.util

import java.util.Calendar

/**
 * Gregorian → Jalali (Solar Hijri) conversion used for the daily challenge date.
 *
 * The app has no external Persian-calendar dependency, so the well-known jalaali-js
 * arithmetic algorithm is implemented here. It is valid for the years the app will
 * realistically run in.
 */
object PersianDate {

    private val gregorianMonthDays =
        intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)

    fun today(): String {
        val calendar = Calendar.getInstance()
        return format(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun format(gy: Int, gm: Int, gd: Int): String {
        val (jy, jm, jd) = toJalali(gy, gm, gd)
        val raw = "%04d/%02d/%02d".format(jy, jm, jd)
        return PersianText.toPersianDigits(raw)
    }

    fun toJalali(gy: Int, gm: Int, gd: Int): Triple<Int, Int, Int> {
        val gy2 = if (gm > 2) gy + 1 else gy
        var days = 355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) +
            ((gy2 + 399) / 400) + gd + gregorianMonthDays[gm - 1]

        var jy = -1595 + (33 * (days / 12053))
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }

        val jm: Int
        val jd: Int
        if (days < 186) {
            jm = 1 + days / 31
            jd = 1 + days % 31
        } else {
            jm = 7 + (days - 186) / 30
            jd = 1 + (days - 186) % 30
        }
        return Triple(jy, jm, jd)
    }
}
