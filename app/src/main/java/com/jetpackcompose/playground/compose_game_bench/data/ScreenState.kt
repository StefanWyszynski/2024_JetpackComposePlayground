package com.jetpackcompose.playground.compose_game_bench.data

import androidx.compose.runtime.Stable

@Stable
data class ScreenState(
    val screenWidth: Int = 100,
    val screenHeight: Int = 100,
    val screenWidthHalf: Float = 0f,
    val screenHeightHalf: Float = 0f,
    val incrementAngle: Double = 60.0 / screenWidth.toDouble(),
    val raycastingPrecision: Double = 64.0
)
