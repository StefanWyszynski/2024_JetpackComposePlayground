package com.jetpackcompose.playground.compose_game_bench.domain.util

import com.google.common.truth.Truth.assertThat
import com.jetpackcompose.playground.compose_game_bench.data.Player
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState
import kotlinx.coroutines.test.UnconfinedTestDispatcher
//import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class RayCastUtilTest {

    // Given
    val rayAngle = 0.0
    lateinit var screenState: ScreenState
    lateinit var map1: List<List<Int>>
    lateinit var player: Player
    lateinit var collInfo: RaycastScreenColumnInfo

    val testDispatcher = UnconfinedTestDispatcher()
    @Before
    fun start() {
        screenState = ScreenState()
        player = Player(x = 2.0, y = 2.0)
        collInfo = RaycastScreenColumnInfo()
        map1 = listOf(
            listOf(1, 1, 1, 1),
            listOf(1, 0, 0, 1),
            listOf(1, 0, 0, 1),
            listOf(1, 1, 1, 1)
        )
    }

    @Test
    fun `castRayInMap should return specific value when player in map range`() {
        // When
        val distanceToWall =
            RayCastUtil(testDispatcher).castRayInMapToFindWalls(player, rayAngle, screenState, map1, collInfo)

        // Then
        // assertEquals(1.0, distanceToWall, 0.01)
        assertThat(distanceToWall).isWithin(0.1).of(1.0)
    }

    @Test
    fun `castRayInMap should return proper worldTextureOffset value when player in map range`() {
        // When
        RayCastUtil(testDispatcher).castRayInMapToFindWalls(player, rayAngle, screenState, map1, collInfo)

        // Then
        // assertEquals(5.0f, collInfo.worldTextureOffset, 0.1f)
        assertThat(collInfo.worldTextureOffset).isWithin(0.1).of(5.0)
    }

    @Test
    fun `castRayInMap should return proper wallHitScale value when player in map range`() {
        RayCastUtil(testDispatcher).castRayInMapToFindWalls(player, rayAngle, screenState, map1, collInfo)

        // assertEquals(1, collInfo.wallHitScale)
        assertThat(collInfo.eyeRayHitWallNumber).isEqualTo(1)
    }

    @Test
    fun `castRayInMap should cast exception when player outside map`() {
        player = Player(x = -200.0, y = -200.0)

        assertThrows(ArrayIndexOutOfBoundsException::class.java) {
            RayCastUtil(testDispatcher).castRayInMapToFindWalls(player, rayAngle, screenState, map1, collInfo)
        }
    }
}