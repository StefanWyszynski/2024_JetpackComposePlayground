package com.jetpackcompose.playground.compose_game_bench.presentation.data

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState

class GameData {
    /**
     * this will be a buffer for screen pixels. Here we cast floor and ceiling. Walls could be cast
     * here but for benchmarking purposes it's drawn separately using drawImage with jetpack compose
      */
    var screenPixelsBuffer: IntArray

    /**
     * screenPixelsBuffer will be converted to this bitmap to represent IntArray as bitmap
     */
    var screenBitmap: ImageBitmap

    /**
     * whole game screen state
     */
    val screenState = ScreenState()

    /**
     * used to hold floor and ceiling textures to render them pixel by pixel into screenPixelsBuffer
     */
    val textures = arrayListOf<IntArray>()

    /**
     * each 1 or 2 in this map is a wall. 0 represents empty space where you will see the floor
     */
    var gameMap: List<List<Int>> = listOf(
        listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 0, 2, 0, 2, 2, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 1),
        listOf(1, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 1),
        listOf(1, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
    )

    init {
        screenBitmap = Bitmap.createBitmap(
            screenState.screenWidth,
            screenState.screenHeight,
            Bitmap.Config.ARGB_8888
        ).asImageBitmap()

        screenPixelsBuffer = IntArray(screenState.screenWidth * screenState.screenHeight)
    }

    fun copyRayCastedScreenBufferToImageAndGet(): ImageBitmap {
        val screenState = screenState

        screenBitmap.asAndroidBitmap().setPixels(
            screenPixelsBuffer, 0, screenState.screenWidth,
            0, 0, screenState.screenWidth, screenState.screenHeight
        )

        return screenBitmap
    }
}