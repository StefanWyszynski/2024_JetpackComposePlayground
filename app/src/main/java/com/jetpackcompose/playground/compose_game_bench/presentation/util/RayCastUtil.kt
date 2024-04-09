package com.jetpackcompose.playground.compose_game_bench.presentation.util

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import com.jetpackcompose.playground.compose_game_bench.presentation.data.PlayerState
import com.jetpackcompose.playground.compose_game_bench.presentation.data.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt


const val maxDistance = 8.0

class RayCastUtil {

    data class RaycastScreenColumnInfo(
        var wallHeight: Double,
        var colorIntensity: Int,
        var xOffset: Float,
        var worldTextureOffset: Float
    )

    inline fun rayCasting(
        screenColumns: List<Int>,
        playerState: State<PlayerState>,
        screenInfo: ScreenState,
        map: List<List<Int>>,
        drawColumn: (
            textureXOffset: Int,
            x1: Float, y1: Float,
            x2: Float, y2: Float,
            colorFar: Color,
            colorNear: Color,
            drawTextured: Boolean,
            worldTextureOffset: Float
        ) -> Unit
    ) {
        val player = playerState.value

        val columns = runBlocking {
            rayTraceColumnsAsync(screenColumns, player, screenInfo, map).awaitAll()
        }

        for (line in columns) {
            val wallHeight = line.wallHeight
            val colorIntensity = line.colorIntensity
            val xoffset = line.xOffset
            val worldTextureOffset = line.worldTextureOffset

            val textureXOffset = 0;
            val skyBlue = Color(0xFF000000 + colorIntensity)
            // draw ceiling
            drawColumn(
                textureXOffset,
                xoffset, 0f, xoffset,
                (screenInfo.screenHeightHalf - wallHeight).toFloat(),
                Color.Blue, skyBlue, false, worldTextureOffset
            )

            val white = Color(0xFF000000 + ((colorIntensity shl 16) or (colorIntensity shl 8) or colorIntensity))
            // draw wall
            drawColumn(
                textureXOffset,
                xoffset, (screenInfo.screenHeightHalf - wallHeight).toFloat(),
                xoffset, (screenInfo.screenHeightHalf + wallHeight).toFloat(),
                white, white, true, worldTextureOffset
            )

            val green = Color(0xFF000000 + ((colorIntensity shl 8)))
            // draw floor
            drawColumn(
                textureXOffset,
                xoffset, (screenInfo.screenHeightHalf + wallHeight).toFloat(),
                xoffset, screenInfo.screenHeight.toFloat(),
                green, Color.Green, false, worldTextureOffset
            )
        }

    }

    fun CoroutineScope.rayTraceColumnsAsync(
        screenColumns: List<Int>,
        player: PlayerState,
        screenInfo: ScreenState,
        map: List<List<Int>>
    ) = screenColumns.map { columnX ->
        async(Dispatchers.Default) {
            var rayAngle = (player.angle - player.halfFov)
            rayAngle += screenInfo.incrementAngle * columnX

            val collInfo = RaycastScreenColumnInfo(0.0, 0, 0f, 0f)
            // Ray data
            val distanceToWall = castRayInMap(player, rayAngle, screenInfo, map, collInfo)

            // Wall height
            val wallHeight = floor(screenInfo.screenHeightHalf / distanceToWall);
            val colorIntensity = calculateColorIntensityByDistance(distanceToWall)
            val xoffset = columnX.toFloat()

            collInfo.xOffset = xoffset
            collInfo.colorIntensity = colorIntensity
            collInfo.wallHeight = wallHeight
            collInfo
        }
    }

    fun castRayInMap(
        player: PlayerState,
        rayAngle: Double,
        screenInfo: ScreenState,
        map: List<List<Int>>,
        collInfo: RaycastScreenColumnInfo
    ): Double {
        var rayX = player.x
        var rayY = player.y

        // Ray path incrementers
        val rayCos = cos(Math.toRadians(rayAngle)) / screenInfo.raycastingPrecision;
        val raySin = sin(Math.toRadians(rayAngle)) / screenInfo.raycastingPrecision;

        // find wall
        var wall = 0;
        while (wall == 0) {
            rayX += rayCos;
            rayY += raySin;

            var mapX = floor(rayX).toInt()
            var mapY = floor(rayY).toInt()
            if (mapX < 0) {
                mapX = map[0].size + mapX
            }
            if (mapY < 0) {
                mapY = map.size + mapY
            }
            wall = map[mapY][mapX];
        }


        collInfo.worldTextureOffset = (rayX + rayY).toFloat()
        val xPow = (player.x - rayX)
        val yPow = (player.y - rayY)
        var distanceToWall = sqrt(xPow * xPow + yPow * yPow)
        // Fish eye fix
        distanceToWall = distanceToWall * cos(Math.toRadians(rayAngle - player.angle));
        return distanceToWall

    }

    fun calculateColorIntensityByDistance(distance: Double): Int {
        val wallMaxFactpr = 1.0 - clampDouble(0.0, 1.0, distance / maxDistance)
        val colorIntensity = lerp(0.0, 255.0, wallMaxFactpr).toInt()
        return colorIntensity
    }

    fun clampDouble(min: Double, max: Double, input: Double): Double {
        return if (input < min) {
            min
        } else {
            if (input > max) max else input
        }
    }

    fun lerp(start: Double, stop: Double, amount: Double): Double {
        return (1.0 - amount) * start + amount * stop
    }
}