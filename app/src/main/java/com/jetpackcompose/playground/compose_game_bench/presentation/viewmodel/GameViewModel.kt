package com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.jetpackcompose.playground.compose_game_bench.presentation.data.PlayerState
import com.jetpackcompose.playground.compose_game_bench.presentation.data.ScreenState
import com.jetpackcompose.playground.compose_game_bench.presentation.data.PlayerPointerAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin


@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {

    val screenInfo = MutableStateFlow(ScreenState())

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    var map: List<List<Int>> = listOf(
        listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
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
    }

    fun setPlayerHealth(health: Float) {
        _playerState.update {
            it.copy(health = health)
        }
    }

    fun movePlayerForward(deltatime: Float) {
        movePlayer(deltatime, 1f)
    }

    fun movePlayerBackward(deltatime: Float) {
        movePlayer(deltatime, -1f)
    }

    private fun movePlayer(deltatime: Float, directionSign: Float) {
        val player = _playerState.value
        val playerCos = cos(Math.toRadians(player.angle)) * player.moveSpeed * deltatime
        val playerSin = sin(Math.toRadians(player.angle)) * player.moveSpeed * deltatime
        val newX = player.x + playerCos * directionSign
        val newY = player.y + playerSin * directionSign

        var mapX = floor(newX + playerCos * player.radius * directionSign).toInt()
        var mapY = floor(newY + playerSin * player.radius * directionSign).toInt()
        if (mapX < 0) {
            mapX = map[0].size + mapX
        }
        if (mapY < 0) {
            mapY = map.size + mapY
        }

        // Collision detection
        if (map[mapY][floor(player.x).toInt()] == 0) {
            _playerState.update {
                it.copy(y = newY)
            }
        }
        if (map[floor(player.y).toInt()][mapX] == 0) {
            _playerState.update {
                it.copy(x = newX)
            }
        }
    }

    fun rotatePlayerLeft(deltatime: Float) {
        _playerState.update {
            it.copy(angle = (it.angle - (it.rotateSpeed * deltatime)) % 360)
        }
    }

    fun rotatePlayerRight(deltatime: Float) {
        _playerState.update {
            it.copy(angle = (it.angle + (it.rotateSpeed * deltatime)) % 360)
        }
    }

    fun handlePlayerPointerAction(playerPointerAction: PlayerPointerAction, deltaTime: Float) {
        when (playerPointerAction) {
            PlayerPointerAction.NONE -> {}
            PlayerPointerAction.MOVE_FORWARD -> {
                movePlayerForward(deltaTime)
            }

            PlayerPointerAction.MOVE_BACKWARD -> {
                movePlayerBackward(deltaTime)
            }

            PlayerPointerAction.ROTATE_LEFT -> {
                rotatePlayerLeft(deltaTime)
            }

            PlayerPointerAction.ROTATE_RIGHT -> {
                rotatePlayerRight(deltaTime)
            }
        }
    }
}