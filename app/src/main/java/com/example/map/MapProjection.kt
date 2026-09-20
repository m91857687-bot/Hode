package com.example.map

import androidx.compose.ui.geometry.Offset

object MapProjection {
    // Standard Equirectangular Projection:
    // Longitude: -180 to 180 -> normalized x: 0.0 to 1.0
    // Latitude: 85 to -60 -> normalized y: 0.0 to 1.0

    private const val MIN_LON = -180.0
    private const val MAX_LON = 180.0
    private const val MIN_LAT = -60.0
    private const val MAX_LAT = 82.0

    fun project(longitude: Double, latitude: Double): Offset {
        val normX = ((longitude - MIN_LON) / (MAX_LON - MIN_LON)).coerceIn(0.0, 1.0).toFloat()
        val normY = ((MAX_LAT - latitude) / (MAX_LAT - MIN_LAT)).coerceIn(0.0, 1.0).toFloat()
        return Offset(normX, normY)
    }

    fun invert(normX: Float, normY: Float): Pair<Double, Double> {
        val lon = normX * (MAX_LON - MIN_LON) + MIN_LON
        val lat = MAX_LAT - (normY * (MAX_LAT - MIN_LAT))
        return Pair(lon, lat)
    }
}
