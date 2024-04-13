package com.jetpackcompose.playground.compose_game_bench.presentation

import android.graphics.Point
import android.graphics.PointF
import android.view.MotionEvent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils
import com.jetpackcompose.playground.R
import com.jetpackcompose.playground.compose_game_bench.presentation.data.DrawLineData
import com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val screenInfo by remember {
        viewModel.screenInfo
    }

    val playerDirection = remember { mutableStateOf(PointF()) }
    var deltaTime by remember { mutableStateOf(0f) }

    var lastFrameTime by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            val currentTime = System.currentTimeMillis()
            deltaTime = (currentTime - lastFrameTime) / 1000f
            lastFrameTime = currentTime
            viewModel.rayCast()
            delay(32)
        }
    }
    val wall1 = ImageBitmap.imageResource(id = R.drawable.wall)
    val wall2 = ImageBitmap.imageResource(id = R.drawable.wall2)

    viewModel.handlePlayerMovement(playerDirection.value, deltaTime)
    DrawGameScreen(
        modifier = Modifier.fillMaxSize(),
        playerDirection,
        { size ->
            viewModel.drawRaycastedDataToScreen() { drawData: DrawLineData ->
                val scaleW = (size.width.toFloat() / (screenInfo.screenWidth.toFloat()))
                    .roundToInt().toFloat()
                val scaleH = size.height.toFloat() / (screenInfo.screenHeight.toFloat())
                if (drawData.drawTextured) {
                    drawTexturedWall(
                        drawData, scaleW, scaleH, if (drawData.hitWall == 1) wall1 else wall2
                    )
                } else {
                    drawFloorAndCeiling(drawData, scaleW, scaleH, size.height)
                }
            }
        }
    )

}

@Composable
fun DrawGameScreen(
    modifier: Modifier, playerDirection: MutableState<PointF>,
    onDraw: DrawScope.(IntSize) -> Unit
) {
    val size = remember {
        mutableStateOf(IntSize(0, 0))
    }
    Box(modifier = modifier.fillMaxSize()) {

        Column(verticalArrangement = Arrangement.Top) {
            Row(modifier = Modifier
                .onSizeChanged {
                    size.value = it
                }
                .fillMaxWidth()
                .fillMaxHeight()
//                .aspectRatio(screenInfo.screenWidth.toFloat() / screenInfo.screenHeight.toFloat())
//                .wrapContentHeight()
            ) {
                Canvas(modifier = modifier) {
                    onDraw(size.value)
                }
            }

        }
        Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            PlayerControlJoystickLayout(playerDirection)
        }
    }
}

private fun DrawScope.drawFloorAndCeiling(
    drawData: DrawLineData, scaleW: Float, scaleH: Float, maxHeight: Int
) {
    with(drawData) {
        // get scaled line start
        val lineStart = getScaledAndClampedLinePoint(lineLeft, lineTop, scaleW, scaleH, maxHeight)

        // get scaled line end
        val lineEnd = getScaledAndClampedLinePoint(lineLeft, lineBottom, scaleW, scaleH, maxHeight)
        drawLine(
            brush = Brush.verticalGradient(
                listOf(colorStart, colorEnd),
                startY = lineStart.y,
                endY = maxHeight.toFloat()
            ),
            start = lineStart,
            end = lineEnd,
            strokeWidth = scaleW + 1f,
            cap = StrokeCap.Square
        )
    }
}

fun DrawScope.drawTexturedWall(
    drawData: DrawLineData, scaleW: Float, scaleH: Float,
    wallTexture: ImageBitmap
) {
    with(drawData) {
        // get scaled line start
        val lineStart = getScaledLinePoint(lineLeft, lineTop, scaleW, scaleH)

        // get scaled line end
        val lineEnd = getScaledLinePoint(lineLeft, lineBottom, scaleW, scaleH)
        val scaleAspectRatio = scaleW/scaleH
        val scaleTextureCoordinate = worldTextureOffset * scaleAspectRatio
        val textureX = (scaleTextureCoordinate * wallTexture.width).roundToInt() % wallTexture.width
        drawImage(
            wallTexture,
            srcOffset = IntOffset(textureX, 0),
            srcSize = IntSize(1, wallTexture.height),
            dstOffset = IntOffset(lineStart.x.toInt(), lineStart.y.toInt()),
            dstSize = IntSize(scaleW.toInt(), (lineEnd.y - lineStart.y).toInt()),
            colorFilter = ColorFilter.tint(colorStart, blendMode = BlendMode.Multiply),
            filterQuality = FilterQuality.Low
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PlayerControlJoystickLayout(playerDirection: MutableState<PointF>) {
    var joystickRestCenter by remember {
        mutableStateOf(Point(0, 0))
    }
    var distanceToJoystickRestCenter by remember {
        mutableStateOf(PointF(0f, 0f))
    }

    var isJoystickPressed by remember { mutableStateOf(false) }

    val scaleJoystickIcon by animateFloatAsState(
        targetValue = if (isJoystickPressed) 1.5f else 1f,
        animationSpec = tween(500, easing = LinearOutSlowInEasing)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .onSizeChanged {
                joystickRestCenter = Point(it.width / 2, it.height / 2)
            }
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        distanceToJoystickRestCenter = PointF()
                        isJoystickPressed = true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        distanceToJoystickRestCenter = PointF(
                            (event.x - joystickRestCenter.x),
                            (event.y - joystickRestCenter.y)
                        )
                        playerDirection.value = PointF(
                            distanceToJoystickRestCenter.x / joystickRestCenter.x,
                            -(distanceToJoystickRestCenter.y) / joystickRestCenter.y
                        )
                    }

                    MotionEvent.ACTION_UP -> {
                        distanceToJoystickRestCenter = PointF()
                        isJoystickPressed = false
                        playerDirection.value = PointF()
                    }
                }
                true
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(painterResource(id = R.drawable.drag_joystick),
            tint = Color.Red,
            contentDescription = "Joystick",
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    val maxX = size.width
                    val maxY = size.height
                    translationX = MathUtils.clamp(distanceToJoystickRestCenter.x, -maxX, maxX)
                    translationY = MathUtils.clamp(distanceToJoystickRestCenter.y, -maxY, maxY)
                    scaleX = scaleJoystickIcon
                    scaleY = scaleJoystickIcon
                }
        )
    }
}

/**
 * scale point x, y, by scaleW and scaleH and clamp to range 0...maxHeight
 */
private fun getScaledAndClampedLinePoint(
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
fun getScaledLinePoint(x: Float, y: Float, scaleW: Float, scaleH: Float): Offset {
    val lineStart = Offset((x * scaleW), (y * scaleH))
    return lineStart
}
