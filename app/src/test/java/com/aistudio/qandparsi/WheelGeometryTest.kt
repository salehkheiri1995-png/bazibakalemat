package com.aistudio.qandparsi

import androidx.compose.ui.geometry.Offset
import com.aistudio.qandparsi.util.WheelGeometry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WheelGeometryTest {

    private val center = Offset(200f, 200f)
    private val radius = 100f
    private val centers = WheelGeometry.nodeCenters(4, center, radius)

    @Test
    fun firstNodeSitsAtTop() {
        assertOffsetEquals(Offset(200f, 100f), centers[0])
        assertOffsetEquals(Offset(300f, 200f), centers[1])
        assertOffsetEquals(Offset(200f, 300f), centers[2])
        assertOffsetEquals(Offset(100f, 200f), centers[3])
    }

    @Test
    fun dragToLeftSelectsLeftNode() {
        // Regression: in an RTL layout the visual nodes used to be mirrored while the
        // touch coordinates were absolute, so dragging left selected the right letter.
        val selected = WheelGeometry.nearestIndex(Offset(100f, 200f), centers, 42f)
        assertEquals(3, selected)
    }

    @Test
    fun dragToRightSelectsRightNode() {
        val selected = WheelGeometry.nearestIndex(Offset(300f, 200f), centers, 42f)
        assertEquals(1, selected)
    }

    @Test
    fun touchOutsideEveryNodeReturnsNull() {
        assertNull(WheelGeometry.nearestIndex(Offset(200f, 10f), centers, 42f))
    }

    @Test
    fun nearestNodeWinsWhenHitAreasOverlap() {
        val crowded = WheelGeometry.nodeCenters(8, center, 20f)
        // A point that is inside both hit circles but clearly closer to node 1.
        // "first match" hit testing would incorrectly return node 0.
        val closerToSecond = Offset(
            crowded[0].x + (crowded[1].x - crowded[0].x) * 0.75f,
            crowded[0].y + (crowded[1].y - crowded[0].y) * 0.75f
        )
        val selected = WheelGeometry.nearestIndex(closerToSecond, crowded, 100f)
        assertEquals(1, selected)
    }

    @Test
    fun angleForStartsAtMinusQuarterTurn() {
        assertEquals(-Math.PI / 2.0, WheelGeometry.angleFor(0, 4), 1e-9)
        assertEquals(0.0, WheelGeometry.angleFor(1, 4), 1e-9)
        assertEquals(Math.PI / 2.0, WheelGeometry.angleFor(2, 4), 1e-9)
    }

    @Test
    fun degenerateCountDoesNotCrash() {
        assertEquals(0.0, WheelGeometry.angleFor(0, 0), 0.0)
        assertEquals(emptyList<Offset>(), WheelGeometry.nodeCenters(0, center, radius))
    }

    private fun assertOffsetEquals(expected: Offset, actual: Offset) {
        assertEquals(expected.x, actual.x, 0.01f)
        assertEquals(expected.y, actual.y, 0.01f)
    }
}
