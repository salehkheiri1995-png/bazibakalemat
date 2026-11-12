package com.aistudio.qandparsi

import com.aistudio.qandparsi.util.PersianDate
import org.junit.Assert.assertEquals
import org.junit.Test

class PersianDateTest {

    @Test
    fun convertsKnownNewYearBoundaries() {
        assertEquals(Triple(1403, 1, 1), PersianDate.toJalali(2024, 3, 20))
        assertEquals(Triple(1404, 1, 1), PersianDate.toJalali(2025, 3, 21))
        assertEquals(Triple(1405, 1, 1), PersianDate.toJalali(2026, 3, 21))
    }

    @Test
    fun convertsKnownAutumnDate() {
        assertEquals(Triple(1405, 8, 21), PersianDate.toJalali(2026, 11, 12))
        assertEquals(Triple(1402, 12, 29), PersianDate.toJalali(2024, 3, 19))
    }

    @Test
    fun formatsWithPersianDigits() {
        assertEquals("۱۴۰۳/۰۱/۰۱", PersianDate.format(2024, 3, 20))
        assertEquals("۱۴۰۵/۰۸/۲۱", PersianDate.format(2026, 11, 12))
    }
}
