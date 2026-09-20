package com.example.map

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

data class CountryPolygon(
    val iso3: String,
    val nameAr: String,
    val rings: List<List<Offset>>, // Normalized coordinates [0f, 1f]
    val boundingBox: Rect,
    val centroid: Offset
) {
    companion object {
        fun fromCoordinates(
            iso3: String,
            nameAr: String,
            rawRingsLonLat: List<List<Pair<Double, Double>>>
        ): CountryPolygon {
            val projectedRings = rawRingsLonLat.map { ring ->
                ring.map { (lon, lat) -> MapProjection.project(lon, lat) }
            }

            var minX = Float.MAX_VALUE
            var maxX = -Float.MAX_VALUE
            var minY = Float.MAX_VALUE
            var maxY = -Float.MAX_VALUE

            var sumX = 0.0
            var sumY = 0.0
            var totalPoints = 0

            for (ring in projectedRings) {
                for (p in ring) {
                    if (p.x < minX) minX = p.x
                    if (p.x > maxX) maxX = p.x
                    if (p.y < minY) minY = p.y
                    if (p.y > maxY) maxY = p.y
                    sumX += p.x
                    sumY += p.y
                    totalPoints++
                }
            }

            val bBox = if (totalPoints > 0) {
                Rect(minX, minY, maxX, maxY)
            } else {
                Rect(0f, 0f, 0f, 0f)
            }

            val center = if (totalPoints > 0) {
                Offset((sumX / totalPoints).toFloat(), (sumY / totalPoints).toFloat())
            } else {
                Offset(0.5f, 0.5f)
            }

            return CountryPolygon(
                iso3 = iso3,
                nameAr = nameAr,
                rings = projectedRings,
                boundingBox = bBox,
                centroid = center
            )
        }
    }
}
