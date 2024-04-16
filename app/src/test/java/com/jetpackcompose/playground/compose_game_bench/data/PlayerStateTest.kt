package com.jetpackcompose.playground.compose_game_bench.data

import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerStateTest {

    val deltaTime = 1f
    val map = listOf(
        listOf(1, 1, 1, 1, 1, 1, 1, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 1, 1, 1, 1, 1, 1, 1)
    )

    @Test
    fun `move player position changed`() {
        // Given
        val moveDirection = 1f
        val player = PlayerState(
            x = 4.0,
            y = 4.0,
            angle = 0.0,
            moveSpeed = 1.0,
            radius = 1.0
        )

        // When
        player.handlePlayerMovement(0f, moveDirection, deltaTime, map)

        // Then
        assertEquals(5.0, player.x, 0.1)
        assertEquals(4.0, player.y, 0.1)

        player.angle = 180.0

        // When
        player.handlePlayerMovement(0f, moveDirection, deltaTime, map)

        // Then
        assertEquals(4.0, player.x, 0.1)
        assertEquals(4.0, player.y, 0.1)
    }

    @Test
    fun `rotate player angle changed`() {
        // Given
        val player = PlayerState(angle = 0.0, rotateSpeed = 90.0)

        // When
        player.handlePlayerMovement(1f, 0f, deltaTime, map)

        // Then
        assertEquals(90.0, player.angle, 0.1)

        // Given
        // When
        player.handlePlayerMovement(-1f, 0f, deltaTime, map)

        // Then
        assertEquals(0.0, player.angle, 0.1)
    }
}