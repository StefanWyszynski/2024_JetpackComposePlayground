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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
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
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState
import com.jetpackcompose.playground.compose_game_bench.presentation.data.DrawWallLineData
import com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun GameScreen(viewModel: GameViewModel) {
    // prepare game states
    val screenState by remember { mutableStateOf(viewModel.gameData.screenState) }
    val playerDirection = remember { mutableStateOf(PointF()) }
    var deltaTime by remember { mutableFloatStateOf(0f) }
    var lastFrameTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // prepare game textures
    val wall1 = ImageBitmap.imageResource(id = R.drawable.wall4)
    val wall2 = ImageBitmap.imageResource(id = R.drawable.wall5)
    val floor = ImageBitmap.imageResource(id = R.drawable.floor)
    val ceiling = ImageBitmap.imageResource(id = R.drawable.sky)

    // update game loop
    LaunchedEffect(Unit) {
        while (true) {
            val currentTime = System.currentTimeMillis()
            deltaTime = (currentTime - lastFrameTime) / 1000f
            lastFrameTime = currentTime
            viewModel.rayCast(floor.width, floor.height)
            delay(16)
        }
    }

    // convert textures to pixel arrays once
    ConvertFloorAndCeilingTexturesToByteArrayData(viewModel, floor, ceiling)

    // update player movement
    viewModel.handlePlayerMovement(playerDirection.value, deltaTime)

    DrawGameScreen(
        modifier = Modifier.fillMaxSize(),
        playerDirection, screenState,
        { realScreenSize ->
            // draw game screen without walls. Walls will be drawn next
            drawRayCastedFloorAndCeiling(viewModel, screenState, realScreenSize)

            // this is drawn intentionally using canvas API. I could draw the wall onto screen
            // texture pixel by pixel as an int array as above, but I have used canvas drawImage
            // to draw each texture line for benchmarking purposes. This is an example. I don't
            // want to make a fully working game :)
            viewModel.drawRaycastedWallsToScreen(realScreenSize) { drawData: DrawWallLineData ->
                drawTexturedWall(
                    drawData,
                    if (drawData.eyeRayHitWallNum == 1) wall1 else wall2,
                    screenState
                )
            }
        }
    )
}

private fun DrawScope.drawRayCastedFloorAndCeiling(
    viewModel: GameViewModel,
    screenState: ScreenState,
    size: IntSize
) {
    val image = viewModel.gameData.copyRayCastedScreenBufferToImageAndGet()
    drawImage(
        image,
        srcOffset = IntOffset(0, 0),
        srcSize = IntSize(screenState.screenWidth, screenState.screenHeight),
        dstOffset = IntOffset(0, 0),
        dstSize = IntSize(size.width, size.height),
        filterQuality = screenState.filteringQuality
    )
}

@Composable
private fun ConvertFloorAndCeilingTexturesToByteArrayData(
    viewModel: GameViewModel, floor: ImageBitmap, ceiling: ImageBitmap
) {
    LaunchedEffect(Unit) {
        launch {
            val textures = viewModel.gameData.textures
            textures.clear()
            textures.add(viewModel.convertImageBitmapToIntArray(floor))
            textures.add(viewModel.convertImageBitmapToIntArray(ceiling))
        }
    }
}

@Composable
fun DrawGameScreen(
    modifier: Modifier, playerDirection: MutableState<PointF>,
    screenState: ScreenState, onDraw: DrawScope.(IntSize) -> Unit
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
//                .aspectRatio(screenState.screenWidth.toFloat() / screenState.screenHeight.toFloat())
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

inline fun DrawScope.drawTexturedWall(
    drawData: DrawWallLineData, wallTexture: ImageBitmap, screenState: ScreenState
) {
    with(drawData) {
//        val scaleAspectRatio = scaleW / scaleH
        val scaleTextureCoordinate = worldTextureOffset //* scaleAspectRatio
        val scaledWorldTextureOffset =
            (scaleTextureCoordinate * wallTexture.width).roundToInt() % wallTexture.width
        val oneDpInPixels = 1.dp.toPx().toInt()
        drawImage(
            wallTexture,
            srcOffset = IntOffset(scaledWorldTextureOffset, 0),
            srcSize = IntSize(1, wallTexture.height),
            dstOffset = IntOffset(wallLineTopLeft.x.toInt(), wallLineTopLeft.y.toInt()),
            dstSize = IntSize(
                virtualGameScreenToPhoneScreenRatioWidth.toInt() + oneDpInPixels,
                (wallLineBottomLeft.y - wallLineTopLeft.y).toInt()
            ),
            colorFilter = ColorFilter.tint(colorStart, blendMode = BlendMode.Multiply),
            filterQuality = screenState.filteringQuality
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
        animationSpec = tween(500, easing = LinearOutSlowInEasing), label = ""
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
