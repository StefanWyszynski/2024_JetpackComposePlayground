package com.jetpackcompose.playground.compose_game_bench.domain.util

import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.ColorUtils
import androidx.core.math.MathUtils
import com.jetpackcompose.playground.compose_game_bench.data.Player
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState
import com.jetpackcompose.playground.compose_game_bench.presentation.data.GameData
import com.jetpackcompose.playground.di.annotations.DispathersDefault
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * scale point x, y, by scaleW and scaleH and clamp to range 0...maxHeight
 */
inline fun getScaledAndClampedLinePoint(
    x: Float, y: Float, scaleW: Float, scaleH: Float, maxHeight: Int
): Offset {
    val scaledY1 = (y * scaleH)
    val clampScaledY = MathUtils.clamp(scaledY1, 0f, maxHeight.toFloat())
    val lineStart = Offset(x * scaleW, clampScaledY)
    return lineStart
}

/**
 * scale point x, y, by scaleW and scaleH and clamp to range 0...maxHeight
 */
inline fun getScaledLinePoint(x: Float, y: Float, scaleW: Float, scaleH: Float): Offset {
    val lineStart = Offset((x * scaleW), (y * scaleH))
    return lineStart
}

class RayCastUtil @Inject constructor(@DispathersDefault val defaultDispatcher: CoroutineDispatcher) {

    private var deferedList = mutableListOf<Deferred<RaycastScreenColumnInfo>>()

    fun rayCastingScreenColumnsInfo(
        screenColumns: List<RaycastScreenColumnInfo>,
        player: Player,
        gameData: GameData,
        textureWidth: Int,
        textureHeight: Int
    ) = runBlocking {
        rayTraceColumnsAsync(
            screenColumns, player,
            gameData,
            textureWidth,
            textureHeight
        ).awaitAll()
    }

    fun CoroutineScope.rayTraceColumnsAsync(
        screenColumns: List<RaycastScreenColumnInfo>,
        player: Player,
        gameData: GameData,
        textureWidth: Int,
        textureHeight: Int
    ): List<Deferred<RaycastScreenColumnInfo>> {
        var cleared = false
        if (deferedList.size != screenColumns.size) {
            deferedList.clear()
            cleared = true
        }
        val screenState = gameData.screenState
        val gameMap = gameData.gameMap
        val texturesData = gameData.textures
        val halfFov = gameData.screenState.halfFov
        if (texturesData.size >= 2) {
            for (screenColumn in screenColumns) {
                val item = async(defaultDispatcher) {
                    var rayAngle = (player.angle - halfFov)
                    rayAngle += screenState.incrementAngle * screenColumn.virtualScreenXLineNumber.toDouble()

                    val distanceToWall =
                        castRayInMapToFindWalls(
                            player,
                            rayAngle,
                            screenState,
                            gameMap,
                            screenColumn
                        )

                    val wallHeight = (screenState.screenHeightHalf / distanceToWall)
                    val colorIntensity = RayCastMathUtils.calculateColorIntensityByDistance(
                        distanceToWall, player.maxViewDistance
                    )
                    screenColumn.castedWallColorIntensity = colorIntensity
                    screenColumn.castedWallHeight = wallHeight

                    drawFloorAndCeil(
                        screenState,
                        player,
                        screenColumn.virtualScreenXLineNumber,
                        wallHeight,
                        rayAngle,
                        textureWidth,
                        textureHeight,
                        gameData.screenPixelsBuffer,
                        texturesData
                    )

                    screenColumn
                }

                if (cleared)
                    deferedList.add(item)
                else {
                    deferedList[screenColumn.virtualScreenXLineNumber] = item
                }
            }
        }
        return deferedList
    }

    inline fun castRayInMapToFindWalls(
        player: Player, rayAngle: Double, screenInfo: ScreenState, map: List<List<Int>>,
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

            val mapX = floor(rayX).toInt()
            val mapY = floor(rayY).toInt()
            if (mapX < 0 || mapY < 0 || mapX >= map[0].size || mapY >= map.size) {
                throw ArrayIndexOutOfBoundsException()
            }
            wall = map[mapY][mapX]
        }

        collInfo.rayAngle = rayAngle
        collInfo.worldTextureOffset = (rayX + rayY)
        collInfo.eyeRayHitWallNumber = wall
        val xPow = (player.x - rayX)
        val yPow = (player.y - rayY)
        var distanceToWall = sqrt(xPow * xPow + yPow * yPow)

        var normalizedAngle = rayAngle - player.angle
        while (normalizedAngle < -180) {
            normalizedAngle += 360
        }
        while (normalizedAngle >= 180) {
            normalizedAngle -= 360
        }
        distanceToWall *= cos(Math.toRadians(normalizedAngle))
        return distanceToWall
    }

    fun drawFloorAndCeil(
        screenState: ScreenState, player: Player,
        x1: Int, wallHeight: Double, rayAngle: Double,
        texWidth: Int, texHeight: Int,
        screenPixelsBuffer: IntArray, texturesData: ArrayList<IntArray>
    ) {
        val start = (screenState.screenHeightHalf + wallHeight - 10).toInt()
        val directionCos = cos(Math.toRadians(rayAngle))
        val directionSin = sin(Math.toRadians(rayAngle))
        val playerAngle = player.angle
        val darkColor = (0xFF000000).toInt()
        val x = playerAngle - rayAngle
        var normalizedAngle = x
        while (normalizedAngle < -180) {
            normalizedAngle += 360
        }
        while (normalizedAngle >= 180) {
            normalizedAngle -= 360
        }
        val angleCos = cos(Math.toRadians(normalizedAngle))
        for (y in start until screenState.screenHeight) {
            var distToPixel =
                screenState.screenHeight / (2 * y - screenState.screenHeight).toDouble()
            distToPixel /= angleCos
            val colorAtDistance =
                MathUtils.clamp(distToPixel / player.maxViewDistance, 0.0, 1.0)

            val tilex = (distToPixel * directionCos) + player.x
            val tiley = (distToPixel * directionSin) + player.y

            val texture_x = abs((Math.floor(tilex * texWidth)) % texWidth).toInt()
            val texture_y = abs((Math.floor(tiley * texHeight)) % texHeight).toInt()

            val floorColor = texturesData[0][texture_x + texture_y * texWidth]
            val ceilColor = texturesData[1][texture_x + texture_y * texWidth]

            val floorColorAtDist =
                ColorUtils.blendARGB(floorColor, darkColor, colorAtDistance.toFloat())
            val ceilColorAtDist =
                ColorUtils.blendARGB(ceilColor, darkColor, colorAtDistance.toFloat())

            screenPixelsBuffer[x1 + y * screenState.screenWidth] = floorColorAtDist
            screenPixelsBuffer[x1 + (screenState.screenHeight - y - 1) * screenState.screenWidth] =
                ceilColorAtDist
        }
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