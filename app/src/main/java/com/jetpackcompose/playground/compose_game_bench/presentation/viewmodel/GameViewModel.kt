package com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel

import android.graphics.PointF
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.compose_game_bench.data.PlayerState
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState
import com.jetpackcompose.playground.compose_game_bench.domain.RayCastUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor(var rayCastInteractor: RayCastUseCaseImpl) :
    ViewModel() {

    private var _screenColumnsOffsets = listOf<Int>()
    private var _screenColumnsData = listOf<RaycastScreenColumnInfo>()

    val screenInfo = MutableStateFlow(ScreenState())

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    var map: List<List<Int>> = listOf(
        listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
    )

    init {
        // Calculated data
        screenInfo.value.updateA(
            screenInfo.value.screenWidth,
            screenInfo.value.screenHeight,
            _playerState.value
        )
        _playerState.update {
            it.copy(halfFov = it.fov / 2f, x = 2.0, y = 2.0)
        }

        _screenColumnsOffsets = (0..screenInfo.value.screenWidth).toList()
    }

    fun setPlayerHealth(health: Float) {
        _playerState.update {
            it.copy(health = health)
        }
    }

    fun rayCast() {
        viewModelScope.launch {
            _screenColumnsData = rayCastInteractor.rayCastingScreenColumnsInfo(
                _screenColumnsOffsets, playerState.value, screenInfo.value, map
            )
        }
    }

    fun drawRaycastedDataToScreen(
        drawColumn: (
            textureXOffset: Int, x1: Float, y1: Float, x2: Float, y2: Float,
            colorFar: Color, colorNear: Color, drawTextured: Boolean, worldTextureOffset: Float
        ) -> Unit
    ) {
        for (line in _screenColumnsData) {
            val wallHeight = line.wallHeight
            val colorIntensity = line.colorIntensity
            val xoffset = line.xOffset
            val worldTextureOffset = line.worldTextureOffset

            val textureXOffset = 0
            val skyBlue = Color(0xFF000000 + colorIntensity)
            val darkToWhiteColor =
                Color(
                    0xFF000000 +
                            ((colorIntensity shl 16) or
                                    (colorIntensity shl 8) or
                                    colorIntensity)
                )

            // draw ceiling
            val wallHitScale = if (line.wallHitScale == 0) 1 else line.wallHitScale
            val wallHeightFromScreenCenter =
                (screenInfo.value.screenHeightHalf - (wallHeight * wallHitScale)).toFloat()


            // draw sky
            drawColumn(
                textureXOffset, xoffset, 0f, xoffset, wallHeightFromScreenCenter,
                Color(0xFFA0A0FF), skyBlue, false, worldTextureOffset
            )

            // draw wall
            drawColumn(
                textureXOffset, xoffset, wallHeightFromScreenCenter,
                xoffset, (screenInfo.value.screenHeightHalf + wallHeight).toFloat(),
                darkToWhiteColor, darkToWhiteColor, true, worldTextureOffset
            )

            // draw floor
            drawColumn(
                textureXOffset, xoffset, (screenInfo.value.screenHeightHalf + wallHeight).toFloat(),
                xoffset, screenInfo.value.screenHeight.toFloat(),
                darkToWhiteColor, Color.White, false, worldTextureOffset
            )
        }
    }

    fun handlePlayerMovement(playerPointerAction: PointF, deltaTime: Float) {
        if (playerPointerAction.x != 0.0f) {
            playerState.value.rotatePlayer(deltaTime, playerPointerAction.x)
        }

        if (playerPointerAction.y != 0.0f) {
            playerState.value.movePlayer(deltaTime, playerPointerAction.y, map)
        }
    }
}