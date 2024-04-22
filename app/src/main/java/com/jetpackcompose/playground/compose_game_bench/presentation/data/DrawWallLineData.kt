package com.jetpackcompose.playground.compose_game_bench.presentation.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * Data drawn
 */
data class DrawWallLineData(
    /**
     * Offset of the world texture in x and y axes
     */
    var worldTextureOffset: Double = 0.0,
    /**
     * start color of the line to draw
     */
    var colorStart: Color = Color.White,
    /**
     * end color of the line to draw
     */
    var colorEnd: Color = Color.White,
    /**
     * wall number hit by the eye ray in current rendering vertical screen line
     */
    var eyeRayHitWallNum: Int = 0,
    /**
     * top left position of the rendering wall on the screen
     */
    var wallLineTopLeft: Offset = Offset(0f, 0f),
    /**
     * bottom left position of the rendering wall on the screen
     */
    var wallLineButtomLeft: Offset = Offset(0f, 0f),

    /**
     * ratio between virtual screen width and phone screen width
     */
    var virtualGameScreenToPhoneScreenRatioWidth: Float = 0f,
    /**
     * ratio between virtual screen height and phone screen height
     */
    var virtualGameScreenToPhoneScreenRatioHeight: Float = 0f
)