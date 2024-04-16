package com.jetpackcompose.playground.compose_game_bench.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.FilterQuality

@Stable
data class ScreenState(
    val screenWidth: Int = 200,
    val screenHeight: Int = 200,
    val screenWidthHalf: Float = screenWidth/2f,
    val screenHeightHalf: Float = screenHeight/2f,
    val raycastingPrecision: Double = 64.0,
    val filteringQuality: FilterQuality = FilterQuality.None,
    var fov: Double = 60.0,
    var halfFov: Double = (fov / 2.0),
    val incrementAngle: Double = fov / screenWidth.toDouble()

)
