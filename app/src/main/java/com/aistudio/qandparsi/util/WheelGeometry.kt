package com.aistudio.qandparsi.util

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Pure geometry for the circular letter wheel.
 *
 * Keeping the maths outside the composable makes the "which node is under the finger"
 * behaviour testable, including the RTL regression where dragging left used to select
 * the node on the right.
 */
object WheelGeometry {

    /** Angle of the node, in radians. Index 0 sits at the top (12 o'clock). */
    fun angleFor(index: Int, count: Int): Double {
        if (count <= 0) return 0.0
        return (2.0 * Math.PI * index / count) - (Math.PI / 2.0)
    }

    fun nodeCenters(count: Int, center: Offset, radius: Float): List<Offset> {
        return List(count) { index ->
            val angle = angleFor(index, count)
            Offset(
                x = center.x + (cos(angle) * radius).toFloat(),
                y = center.y + (sin(angle) * radius).toFloat()
            )
        }
    }

    fun distance(a: Offset, b: Offset): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx * dx + dy * dy)
    }

    /**
     * Returns the closest node within [hitRadius] of [position], or null when the touch is
     * outside every letter. Choosing the nearest node (rather than the first match) keeps
     * overlapping hit circles unambiguous.
     */
    fun nearestIndex(position: Offset, centers: List<Offset>, hitRadius: Float): Int? {
        var bestIndex: Int? = null
        var bestDistance = Float.MAX_VALUE
        centers.forEachIndexed { index, center ->
            val candidate = distance(position, center)
            if (candidate <= hitRadius && candidate < bestDistance) {
                bestDistance = candidate
                bestIndex = index
            }
        }
        return bestIndex
    }
}
