package com.jetpackcompose.playground.compose_game_bench.domain.util

import com.jetpackcompose.playground.compose_game_bench.data.PlayerState
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

class RayCastUtil @Inject constructor() {

    fun rayCastingScreenColumnsInfo(
        screenColumns: List<Int>, playerState: PlayerState, screenInfo: ScreenState,
        map: List<List<Int>>
    ) = runBlocking {
        rayTraceColumnsAsync(screenColumns, playerState, screenInfo, map).awaitAll()
    }

    fun CoroutineScope.rayTraceColumnsAsync(
        screenColumns: List<Int>, player: PlayerState, screenInfo: ScreenState, map: List<List<Int>>
    ) = screenColumns.map { columnX ->
        async(Dispatchers.Default) {
            var rayAngle = (player.angle - player.halfFov)
            rayAngle += screenInfo.incrementAngle * columnX

            val collInfo = RaycastScreenColumnInfo()
            // Ray data
            val distanceToWall = castRayInMap(player, rayAngle, screenInfo, map, collInfo)

            // Wall height
            val wallHeight = (screenInfo.screenHeightHalf / distanceToWall)
            val colorIntensity = RayCastMathUtils.calculateColorIntensityByDistance(
                distanceToWall,
                player.maxViewDistance
            )
            val xoffset = columnX.toFloat()

            collInfo.xOffset = xoffset
            collInfo.colorIntensity = colorIntensity
            collInfo.wallHeight = wallHeight
            collInfo
        }
    }

    fun castRayInMap(
        player: PlayerState, rayAngle: Double, screenInfo: ScreenState, map: List<List<Int>>,
        collInfo: RaycastScreenColumnInfo
    ): Double {
        var rayX = player.x
        var rayY = player.y

        // Ray path incrementers
        val rayCos = cos(Math.toRadians(rayAngle)) / screenInfo.raycastingPrecision
        val raySin = sin(Math.toRadians(rayAngle)) / screenInfo.raycastingPrecision

        // find wall
        var wall = 0
        while (wall == 0) {
            rayX += rayCos
            rayY += raySin

            var mapX = floor(rayX).toInt()
            var mapY = floor(rayY).toInt()
            if (mapX < 0) {
                mapX = map[0].size + mapX
            }
            if (mapY < 0) {
                mapY = map.size + mapY
            }
            wall = map[mapY][mapX]
        }


        collInfo.worldTextureOffset = (rayX + rayY)
        collInfo.hitWallNumber = wall
        val xPow = (player.x - rayX)
        val yPow = (player.y - rayY)
        var distanceToWall = sqrt(xPow * xPow + yPow * yPow)
        // Fish eye fix
        distanceToWall *= fishEyeFixFactor(rayAngle - player.angle)
        return distanceToWall
    }

    fun fishEyeFixFactor(angle: Double): Double {
        var normalizedAngle = angle
        while (normalizedAngle < -180) {
            normalizedAngle += 360
        }
        while (normalizedAngle >= 180) {
            normalizedAngle -= 360
        }
        return cos(Math.toRadians(normalizedAngle))
    }

    fun myFizzBuzzImplForFun(n: Int): MutableList<String> {
        val fizzBuzzOccuranceText =
            arrayListOf("Fizz", "Buzz", "Fizz", "Fizz", "Buzz", "Fizz", "FizzBuzz")
        val fizzBuzzValues = arrayListOf(3, 5, 3, 3, 5, 3, 3)
        var index = 0
        var currentNum = 3
        val result = mutableListOf<String>()
        for (i in 1..n) {
            if (i % currentNum == 0) {
                result.add(fizzBuzzOccuranceText[index])
                index = (index + 1) % 7
                currentNum = fizzBuzzValues[index]
            } else {
                result.add(i.toString())
            }
        }
        return result
    }
}