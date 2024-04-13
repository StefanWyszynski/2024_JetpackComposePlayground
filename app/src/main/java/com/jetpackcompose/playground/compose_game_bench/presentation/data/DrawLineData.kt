package com.jetpackcompose.playground.compose_game_bench.presentation.data

import androidx.compose.ui.graphics.Color

data class DrawLineData(
    var lineTop: Float = 0f,
    var lineBottom: Float = 0f,
    var lineLeft: Float = 0f,
    var drawTextured: Boolean = false,
    var worldTextureOffset: Double = 0.0,
    var colorStart: Color = Color.White,
    var colorEnd: Color = Color.White,
    var hitWall: Int = 0
)