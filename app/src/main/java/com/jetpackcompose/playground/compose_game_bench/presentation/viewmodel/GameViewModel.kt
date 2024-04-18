package com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel

import android.graphics.PointF
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.compose_game_bench.data.Player
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.domain.RayCastUseCaseImpl
import com.jetpackcompose.playground.compose_game_bench.presentation.data.DrawLineData
import com.jetpackcompose.playground.compose_game_bench.presentation.data.GameData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(var rayCastInteractor: RayCastUseCaseImpl) :
    ViewModel() {

    lateinit var gameData: GameData
    private var _screenColumnsOffsets = listOf<RaycastScreenColumnInfo>()
    private var _screenColumnsData = listOf<RaycastScreenColumnInfo>()

    private val _player = MutableStateFlow(Player())
    val playerState = _player.asStateFlow()

    init {
        gameData = GameData()
        _player.update {
            it.copy(x = 2.0, y = 2.0)
        }

        _screenColumnsOffsets =
            (0..(gameData.screenState.screenWidth - 1)).toList()
                .map { RaycastScreenColumnInfo(xOffset = it) }
    }

    fun setPlayerHealth(health: Float) {
        _player.update {
            it.copy(health = health)
        }
    }

    fun rayCast(textureWidth: Int, textureHeight: Int) {
        viewModelScope.launch {
            _screenColumnsData = rayCastInteractor.rayCastingScreenColumnsInfo(
                _screenColumnsOffsets, playerState.value, gameData, textureWidth, textureHeight
            )
        }
    }

    fun drawRaycastedWallsToScreen(drawColumn: (drawData: DrawLineData) -> Unit) {
        val drawLineData = DrawLineData()
        val screenState = gameData.screenState

        for (line in _screenColumnsData) {
            val wallHeight = line.wallHeight.toFloat()
            val colorIntensity = line.colorIntensity

            val darkToWhiteColor =
                Color(
                    0xFF000000 +
                            ((colorIntensity shl 16) or
                                    (colorIntensity shl 8) or
                                    colorIntensity)
                )
            val wallTop = (screenState.screenHeightHalf - wallHeight)
            val wallBottom = (screenState.screenHeightHalf + wallHeight)
            drawLineData.apply {
                lineLeft = line.xOffset.toFloat()
                lineTop = wallTop
                lineBottom = wallBottom
                this.worldTextureOffset = line.worldTextureOffset
                colorStart = darkToWhiteColor
                colorEnd = darkToWhiteColor
                this.hitWall = line.hitWallNumber
            }

            drawColumn(drawLineData)
        }
    }

    fun handlePlayerMovement(playerPointerAction: PointF, deltaTime: Float) {
        playerState.value.handlePlayerMovement(playerPointerAction.x, playerPointerAction.y, deltaTime, gameData.gameMap)
    }

    suspend fun convertImageBitmapToIntArray(image: ImageBitmap): IntArray {
        val texArrayAsync = viewModelScope.async {
            val textureWidth = image.width
            val textureHeight = image.height

            val textureArray = IntArray(textureWidth * textureHeight)
            image.readPixels(textureArray, 0, 0, textureWidth, textureHeight, 0, textureWidth)
            textureArray
        }
        return texArrayAsync.await()
    }

}