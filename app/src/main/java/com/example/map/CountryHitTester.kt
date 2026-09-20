package com.example.map

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

object CountryHitTester {

    /**
     * Ray-Casting Point-in-Polygon algorithm:
     * Counts the number of times a horizontal ray from the tap point intersects the polygon edges.
     * Odd count = inside, Even count = outside.
     */
    fun findHitCountryIso3(
        normalizedTap: Offset,
        polygons: List<CountryPolygon>
    ): String? {
        val tapX = normalizedTap.x
        val tapY = normalizedTap.y

        // 1. Direct Point-In-Polygon check
        for (poly in polygons) {
            val b = poly.boundingBox
            // Fast bounding box rejection check
            if (tapX < b.left || tapX > b.right || tapY < b.top || tapY > b.bottom) {
                continue
            }

            for (ring in poly.rings) {
                if (isPointInsidePolygonRing(tapX, tapY, ring)) {
                    return poly.iso3
                }
            }
        }

        // 2. Proximity fallback for small countries or near-border clicks (e.g. Lebanon, Qatar, Bahrain, Singapore)
        var closestIso3: String? = null
        var minDistance = 0.035f // ~3.5% screen threshold

        for (poly in polygons) {
            val dx = tapX - poly.centroid.x
            val dy = tapY - poly.centroid.y
            val dist = sqrt(dx * dx + dy * dy)
            if (dist < minDistance) {
                minDistance = dist
                closestIso3 = poly.iso3
            }
        }

        return closestIso3
    }

    private fun isPointInsidePolygonRing(x: Float, y: Float, ring: List<Offset>): Boolean {
        var inside = false
        val n = ring.size
        if (n < 3) return false

        var j = n - 1
        for (i in 0 until n) {
            val pi = ring[i]
            val pj = ring[j]

            // Check if ray crosses edge between pi and pj
            val intersect = ((pi.y > y) != (pj.y > y)) &&
                    (x < (pj.x - pi.x) * (y - pi.y) / (pj.y - pi.y) + pi.x)
            if (intersect) {
                inside = !inside
            }
            j = i
        }
        return inside
    }
}
