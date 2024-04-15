package com.jetpackcompose.playground.compose_game_bench.domain.util

import androidx.core.math.MathUtils

object RayCastMathUtils {

    inline fun calculateColorIntensityByDistance(distance: Double, maxViewDistance: Double): Int {
        val wallMaxFactpr = 1.0 - MathUtils.clamp(distance / maxViewDistance, 0.0, 1.0)
        val colorIntensity = lerp(0.0, 255.0, wallMaxFactpr).toInt()
        return colorIntensity
    }

    inline fun lerp(start: Double, stop: Double, amount: Double): Double {
        return (1.0 - amount) * start + amount * stop
    }
}