package com.jetpackcompose.playground.compose_game_bench.presentation.data

import androidx.compose.runtime.Stable

@Stable
data class ScreenState(
    var screenWidth: Int = 100,
    var screenHeight: Int = 100,
    var screenWidthHalf: Float = 0f,
    var screenHeightHalf: Float = 0f,
    var incrementAngle: Double = 0.0,
    var raycastingPrecision: Double = 64.0
) {
    fun updateA(
        w: Int, h: Int, player: PlayerState
    ) {
        screenWidth = w
        screenHeight = h
        screenWidthHalf = screenWidth / 2f;
        screenHeightHalf = screenHeight / 2f;
        incrementAngle = player.fov / screenWidth.toDouble();
    }
}
