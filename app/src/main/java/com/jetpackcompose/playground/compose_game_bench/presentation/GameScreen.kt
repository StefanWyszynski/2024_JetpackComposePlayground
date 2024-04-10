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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetpackcompose.playground.R
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState
import com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    viewModel: GameViewModel
) {
    val screenInfo = viewModel.screenInfo.collectAsStateWithLifecycle()

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

    DrawGameScreen(
        modifier = Modifier.fillMaxSize(),
        screenInfo.value,
        playerDirection,
        viewModel
    )

    viewModel.handlePlayerMovement(playerDirection.value, deltaTime)
}

@Composable
fun DrawGameScreen(
    modifier: Modifier, screenInfo: ScreenState, playerDirection: MutableState<PointF>,
    viewModel: GameViewModel
) {
    val size = remember {
        mutableStateOf(IntSize(0, 0))
    }
    val wallTexture = ImageBitmap.imageResource(id = R.drawable.wall)
    Box(modifier = modifier.fillMaxSize()) {

        Column(verticalArrangement = Arrangement.Top) {
            Row(modifier = Modifier
                .onSizeChanged {
                    size.value = it
                }
                .fillMaxWidth()
                .fillMaxHeight()
//                .aspectRatio(screenInfo.value.screenWidth.toFloat() / screenInfo.value.screenHeight.toFloat())
//                .wrapContentHeight()
            ) {
                Canvas(modifier = modifier) {
                    viewModel.drawRaycastedDataToScreen() { textureXOffset: Int, x1: Float, y1: Float,
                                                            x2: Float, y2: Float,
                                                            colorFar: Color, colorNear: Color,
                                                            drawTextured: Boolean,
                                                            worldTextureOffset: Float ->
                        val maxWidth = size.value.width
                        val maxHeight = size.value.height

                        val scaleW = maxWidth.toFloat() / (screenInfo.screenWidth.toFloat())
                        val scaleH = maxHeight.toFloat() / (screenInfo.screenHeight.toFloat())

                        if (drawTextured) {
                            drawTexturedWall(
                                x1, y1, scaleW, scaleH,
                                x2, y2, worldTextureOffset, wallTexture, colorFar
                            )
                        } else {
                            drawFloorAndCeiling(
                                x1, y1, scaleW, scaleH, maxHeight, x2, y2, colorFar, colorNear
                            )
                        }
                    }
                }
            }

        }
        Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            PlayerControlJoystickLayout(playerDirection)
        }
    }
}

private fun DrawScope.drawFloorAndCeiling(
    x1: Float, y1: Float, scaleW: Float, scaleH: Float, maxHeight: Int, x2: Float,
    y2: Float, colorFar: Color, colorNear: Color
) {
    // get scaled line start
    val lineStart =
        getScaledAndClampedLinePoint(x1, y1, scaleW, scaleH, maxHeight)

    // get scaled line end
    val lineEnd =
        getScaledAndClampedLinePoint(x2, y2, scaleW, scaleH, maxHeight)
    drawLine(
        brush = Brush.verticalGradient(listOf(colorFar, colorNear)),
        start = lineStart,
        end = lineEnd,
        strokeWidth = scaleW,
        cap = StrokeCap.Square
    )
}

private fun DrawScope.drawTexturedWall(
    x1: Float, y1: Float, scaleW: Float, scaleH: Float, x2: Float,
    y2: Float, worldTextureOffset: Float, wallTexture: ImageBitmap, colorFar: Color
) {
    // get scaled line start
    val lineStart = getScaledLinePoint(x1, y1, scaleW, scaleH)

    // get scaled line end
    val lineEnd = getScaledLinePoint(x2, y2, scaleW, scaleH)
    val scaleAspectRatio = scaleW / scaleH
    val textureX =
        ((worldTextureOffset * scaleAspectRatio) * wallTexture.width).toInt() % wallTexture.width
    drawImage(
        wallTexture,
        srcOffset = IntOffset(textureX, 0),
        srcSize = IntSize(1, wallTexture.height),
        dstOffset = IntOffset(lineStart.x.toInt(), lineStart.y.toInt()),
        dstSize = IntSize(
            scaleW.toInt(), (lineEnd.y - lineStart.y).toInt()
        ),
        colorFilter = ColorFilter.tint(
            colorFar, blendMode = BlendMode.Multiply
        )
    )
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
            tint = Color.Black,
            contentDescription = "Arrow",
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
    val scaledY1 = (y * scaleH).toInt()
    val clampScaledY = MathUtils.clamp(scaledY1, 0, maxHeight).toFloat()
    val lineStart = Offset(x * scaleW, clampScaledY)
    return lineStart
}

/**
 * scale point x, y, by scaleW and scaleH and clamp to range 0...maxHeight
 */
private fun getScaledLinePoint(x: Float, y: Float, scaleW: Float, scaleH: Float): Offset {
    val lineStart = Offset(x * scaleW, (y * scaleH))
    return lineStart
}
