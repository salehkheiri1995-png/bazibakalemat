package com.aistudio.qandparsi

import com.aistudio.qandparsi.util.PersianText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PersianTextTest {

    @Test
    fun arabicYehAndKafAreNormalizedToPersian() {
        assertEquals(PersianText.normalize("كتاب"), PersianText.normalize("کتاب"))
        assertEquals(PersianText.normalize("ياسمين"), PersianText.normalize("یاسمین"))
    }

    @Test
    fun alefVariantsAreEquivalent() {
        assertEquals(PersianText.normalize("آسمان"), PersianText.normalize("اسمان"))
        assertEquals(PersianText.normalize("أحمد"), PersianText.normalize("احمد"))
        assertEquals(PersianText.normalize("إمام"), PersianText.normalize("امام"))
    }

    @Test
    fun zeroWidthAndBidiMarksAreIgnored() {
        assertEquals("می‌روم", "می\u200Cروم")
        assertTrue(PersianText.matches("می\u200Cروم", "میروم"))
        assertTrue(PersianText.matches("\u200Fسلام\u200F", "سلام"))
        assertTrue(PersianText.matches("\uFEFFقند", "قند"))
    }

    @Test
    fun whitespaceIsCollapsedAndTrimmed() {
        assertEquals("قند پارسی", PersianText.normalize("  قند   پارسی  "))
    }

    @Test
    fun hehAndWawVariantsAreNormalized() {
        assertEquals(PersianText.normalize("خانۀ من"), PersianText.normalize("خانه من"))
        assertEquals(PersianText.normalize("مؤمن"), PersianText.normalize("مومن"))
    }

    @Test
    fun matchesRejectsDifferentWords() {
        assertFalse(PersianText.matches("سلام", "کلام"))
    }

    @Test
    fun latinDigitsBecomePersianDigits() {
        assertEquals("۱۴۰۵/۰۸/۲۱", PersianText.toPersianDigits("1405/08/21"))
    }

    @Test
    fun arabicIndicDigitsBecomePersianDigits() {
        // Arabic-Indic digits ٠-٩ map to Persian digits in normalized text.
        assertEquals("۱۴۰۵", PersianText.normalize("١٤٠٥"))
    }

    @Test
    fun combiningHamzaMarksAreIgnored() {
        // U+0654 (HAMZA ABOVE) / U+0655 (HAMZA BELOW) are combining marks and are stripped.
        assertEquals("مسله", PersianText.normalize("مس\u0654له"))
        assertEquals(
            PersianText.normalize("مسئ\u0654له"),
            PersianText.normalize("مسئله")
        )
    }
}
