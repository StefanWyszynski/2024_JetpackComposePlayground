package com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel

import android.graphics.PointF
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.compose_game_bench.data.PlayerState
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState
import com.jetpackcompose.playground.compose_game_bench.domain.RayCastUseCaseImpl
import com.jetpackcompose.playground.compose_game_bench.presentation.data.DrawLineData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(var rayCastInteractor: RayCastUseCaseImpl) :
    ViewModel() {

    private var _screenColumnsOffsets = listOf<RaycastScreenColumnInfo>()
    private var _screenColumnsData = listOf<RaycastScreenColumnInfo>()

    val screenInfo = mutableStateOf(ScreenState())

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    var map: List<List<Int>> = listOf(
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
        // Calculated data
        screenInfo.value = ScreenState(
            screenWidth = screenInfo.value.screenWidth,
            screenHeight = screenInfo.value.screenHeight,
            screenWidthHalf = screenInfo.value.screenWidth / 2f,
            screenHeightHalf = screenInfo.value.screenHeight / 2f,
            incrementAngle = _playerState.value.fov / screenInfo.value.screenWidth.toDouble()
        )
        _playerState.update {
            it.copy(halfFov = (it.fov / 2.0), x = 2.0, y = 2.0)
        }

        _screenColumnsOffsets = (0..screenInfo.value.screenWidth).toList().map { RaycastScreenColumnInfo(xOffset = it) }
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

    fun drawRaycastedDataToScreen(drawColumn: (drawData: DrawLineData) -> Unit) {
        val drawLineData = DrawLineData()
        for (line in _screenColumnsData) {
            val wallHeight = line.wallHeight.toFloat()
            val colorIntensity = line.colorIntensity

            val skyBlue = Color(0xFF000000 + colorIntensity)
            val darkToWhiteColor =
                Color(
                    0xFF000000 +
                            ((colorIntensity shl 16) or
                                    (colorIntensity shl 8) or
                                    colorIntensity)
                )

            // draw ceiling
//            val wallTop = (screenInfo.value.screenHeightHalf - wallHeight * line.hitWallNumber)
            val wallTop = (screenInfo.value.screenHeightHalf - wallHeight)
            val wallBottom = (screenInfo.value.screenHeightHalf + wallHeight)

            // draw sky
            drawLineData.apply {
                lineLeft = line.xOffset.toFloat()
                lineTop = 0f
                lineBottom = wallTop
                drawTextured = false
                this.worldTextureOffset = line.worldTextureOffset
                colorStart = Color(0xFFA0A0FF)
                colorEnd = skyBlue
                this.hitWall = line.hitWallNumber
            }
            drawColumn(drawLineData)

            // draw wall
            drawLineData.apply {
                lineTop = wallTop
                lineBottom = wallBottom
                colorStart = darkToWhiteColor
                colorEnd = darkToWhiteColor
                drawTextured = true
            }

            drawColumn(drawLineData)

            // draw floor
            drawLineData.apply {
                lineTop = wallBottom
                lineBottom = screenInfo.value.screenHeight.toFloat()
                colorStart = darkToWhiteColor
                colorEnd = Color.White
                drawTextured = false
            }
            drawColumn(drawLineData)
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