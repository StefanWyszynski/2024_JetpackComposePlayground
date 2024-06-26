package com.jetpackcompose.playground.compose_game_bench.data

import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

data class Player(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var angle: Double = 0.0,
    var health: Float = 100f,
    var moveSpeed: Double = 3.0,
    var rotateSpeed: Double = 100.0,
    val radius: Double = 5.0,
    val maxViewDistance: Double = 8.0
) {

    private fun movePlayer(deltatime: Float, moveDirection: Float, map: List<List<Int>>) {
        val playerCos = cos(Math.toRadians(angle)) * moveSpeed * deltatime
        val playerSin = sin(Math.toRadians(angle)) * moveSpeed * deltatime
        val newX = x + playerCos * moveDirection
        val newY = y + playerSin * moveDirection

        var mapX = floor(newX + playerCos * radius * moveDirection).toInt()
        var mapY = floor(newY + playerSin * radius * moveDirection).toInt()
        if (mapX < 0) {
            mapX = map[0].size + mapX
        }
        if (mapY < 0) {
            mapY = map.size + mapY
        }

        // Collision detection
        if (map[mapY][floor(x).toInt()] == 0) {
            y = newY
        }
        if (map[floor(y).toInt()][mapX] == 0) {
            x = newX
        }
    }

    private fun rotatePlayer(deltatime: Float, rotateDirection: Float) {
        angle = (angle + (rotateSpeed * rotateDirection * deltatime)) % 360
    }

    fun handlePlayerMovement(x: Float, y: Float, deltaTime: Float, map: List<List<Int>>) {
        if (x != 0.0f) {
            rotatePlayer(deltaTime, x)
        }

        if (y != 0.0f) {
            movePlayer(deltaTime, y, map)
        }
    }
}