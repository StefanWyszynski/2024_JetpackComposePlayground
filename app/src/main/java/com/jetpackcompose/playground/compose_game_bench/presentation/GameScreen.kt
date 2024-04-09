package com.jetpackcompose.playground.compose_game_bench.presentation

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetpackcompose.playground.R
import com.jetpackcompose.playground.compose_game_bench.presentation.data.PlayerPointerAction
import com.jetpackcompose.playground.compose_game_bench.presentation.data.PlayerState
import com.jetpackcompose.playground.compose_game_bench.presentation.data.ScreenState
import com.jetpackcompose.playground.compose_game_bench.presentation.util.RayCastUtil
import com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.delay


@Composable
fun GameScreen(
    viewModel: GameViewModel
) {
    val playerState = viewModel.playerState.collectAsStateWithLifecycle()
    val screenInfo = viewModel.screenInfo.collectAsStateWithLifecycle()

    val playerPointerAction = remember { mutableStateOf(PlayerPointerAction.NONE) }
    var deltaTime by remember { mutableStateOf(0f) }

    var lastFrameTime by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            val currentTime = System.currentTimeMillis()
            deltaTime = (currentTime - lastFrameTime) / 1000f
            lastFrameTime = currentTime
            delay(32)
        }
    }
    DrawGameScreen(
        modifier = Modifier.fillMaxSize(),
        playerState,
        screenInfo,
        viewModel.map,
        playerPointerAction
    )

    viewModel.handlePlayerPointerAction(playerPointerAction.value, deltaTime)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawGameScreen(
    modifier: Modifier,
    playerState: State<PlayerState>,
    screenInfo: State<ScreenState>,
    map: List<List<Int>>,
    playerPointerAction: MutableState<PlayerPointerAction>,
) {
    val size = remember {
        mutableStateOf(IntSize(0, 0))
    }
    val screenColumns by remember(screenInfo) {
        val toList = (0..screenInfo.value.screenWidth).toList()
        mutableStateOf(toList)
    }
    val wall = ImageBitmap.imageResource(id = R.drawable.wall)
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
                    RayCastUtil().rayCasting(
                        screenColumns, playerState, screenInfo.value, map
                    ) { textureXOffset: Int, x1: Float, y1: Float, x2: Float, y2: Float, colorFar: Color, colorNear: Color, drawTextured: Boolean, worldTextureOffset: Float ->
                        val maxWidth = size.value.width
                        val maxHeight = size.value.height

                        val scaleW = maxWidth / (screenInfo.value.screenWidth.toFloat())
                        val scaleH = maxHeight / (screenInfo.value.screenHeight.toFloat())

                        if (drawTextured) {

                            // get scaled line start
                            val lineStart = getScaledLinePoint(x1, y1, scaleW, scaleH, maxHeight)

                            // get scaled line end
                            val lineEnd = getScaledLinePoint(x2, y2, scaleW, scaleH, maxHeight)
                            val textureX = (worldTextureOffset * wall.width).toInt() % wall.width
                            drawImage(
                                wall,
                                srcOffset = IntOffset(textureX, 0),
                                srcSize = IntSize(1, wall.height),
                                dstOffset = IntOffset(lineStart.x.toInt(), lineStart.y.toInt()),
                                dstSize = IntSize(
                                    scaleW.toInt(), (lineEnd.y - lineStart.y).toInt()
                                ),
                                colorFilter = ColorFilter.tint(
                                    colorFar, blendMode = BlendMode.Multiply
                                )
                            )
                        } else {
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
                    }
                }
            }

        }
        Column(modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.weight(1f))

            drawButtonsLayout(playerPointerAction)
        }
    }
}

@Composable
private fun drawButtonsLayout(playerPointerAction: MutableState<PlayerPointerAction>) {
    LazyVerticalGrid(columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.wrapContentHeight(),
        content = {
            item {

                Text(text = "")
            }
            item() {
                AddPlayerActionButtonButton(
                    Modifier.fillMaxSize(1f),
                    playerPointerAction,
                    PlayerPointerAction.MOVE_FORWARD,
                    Icons.Default.KeyboardArrowUp
                )
            }
            item {

                Text(text = "")
            }
            item() {

                AddPlayerActionButtonButton(
                    Modifier.fillMaxSize(1f),
                    playerPointerAction,
                    PlayerPointerAction.ROTATE_LEFT,
                    Icons.Default.KeyboardArrowLeft
                )
            }
            item() {
                AddPlayerActionButtonButton(
                    Modifier.fillMaxSize(1f),
                    playerPointerAction,
                    PlayerPointerAction.MOVE_BACKWARD,
                    Icons.Default.KeyboardArrowDown
                )
            }

            item() {
                AddPlayerActionButtonButton(
                    Modifier.fillMaxSize(1f),
                    playerPointerAction,
                    PlayerPointerAction.ROTATE_RIGHT,
                    Icons.Default.KeyboardArrowRight
                )
            }
        })
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
private fun getScaledLinePoint(
    x: Float, y: Float, scaleW: Float, scaleH: Float, maxHeight: Int
): Offset {
    val lineStart = Offset(x * scaleW, (y * scaleH))
    return lineStart
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun AddPlayerActionButtonButton(
    modifier: Modifier,
    playerPointerAction: MutableState<PlayerPointerAction>,
    actionOnDown: PlayerPointerAction,
    image: ImageVector
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(color = Color.Green, shape = CutCornerShape(5.dp))
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        playerPointerAction.value = actionOnDown
                    }

                    MotionEvent.ACTION_UP -> {
                        playerPointerAction.value = PlayerPointerAction.NONE
                    }
                }
                true
            }, contentAlignment = Alignment.Center
    ) {
        Icon(
            image, contentDescription = "Arrow", modifier = modifier.size(64.dp)
        )
    }
}