package com.jetpackcompose.playground.camerax.presentation

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetpackcompose.playground.camerax.presentation.viewmodel.CameraXViewModel
import com.jetpackcompose.playground.common.presentation.components.CustomTopAppBar
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.utils.ThreadOperation


@Composable
fun CameraXScreenContent(
    customTopAppBarData: CustomTopAppBarData,
    hiltViewModel: CameraXViewModel
) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val camController = remember {
        LifecycleCameraController(context)
    }

    val capturedImage by hiltViewModel.receivedImageSharedFlow.collectAsStateWithLifecycle(
        null
    )
    val hasImage by remember(capturedImage) {
        mutableStateOf(capturedImage != null)
    }
    Scaffold(
        topBar = {
            CustomTopAppBar(customTopAppBarData)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                if (hasImage) {
                    hiltViewModel.deleteImage()
                } else {
                    takePicture(context, camController, hiltViewModel::onReceiveImage)
                }
            }) {
                Icon(
                    imageVector = if (!hasImage) Icons.Default.Add else Icons.Default.Delete,
                    contentDescription = "take image"
                )
            }
        }) { scaffoldPading ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPading)
        )
        {
            when (capturedImage) {
                is ThreadOperation.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }

                is ThreadOperation.Success -> CameraCapturedImageScreenContent(
                    (capturedImage as ThreadOperation.Success<ImageBitmap>).data
                )

                is ThreadOperation.Failure,
                null -> CameraScreenContent(camController, owner, hiltViewModel::onReceiveImage)
            }
        }
    }
}

private fun takePicture(
    context: Context, camController: LifecycleCameraController,
    onReceiveImage: (ImageProxy) -> Unit
) {
    val mainExecutor = ContextCompat.getMainExecutor(context)
    camController.takePicture(mainExecutor, object :
        ImageCapture.OnImageCapturedCallback() {
        @OptIn(ExperimentalGetImage::class)
        override fun onCaptureSuccess(image: ImageProxy) {
            onReceiveImage(image)
        }
    })
}

private fun analyzeScreenImageRealtime(
    context: Context, camController: LifecycleCameraController,
    onReceiveImage: (ImageProxy) -> Unit
) {
    val mainExecutor = ContextCompat.getMainExecutor(context)
    camController.setImageAnalysisAnalyzer(mainExecutor, object : ImageAnalysis.Analyzer {
        @OptIn(ExperimentalGetImage::class)
        override fun analyze(image: ImageProxy) {
            Log.e("TAG", "analyze: ${image.width}x${image.height}")
            image.close()
        }
    })
}


@Composable
fun CameraCapturedImageScreenContent(bitmap: ImageBitmap) {
    Image(
        bitmap = bitmap,
        contentDescription = "CapturedImage",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillHeight
    )
}

@Composable
fun CameraScreenContent(
    camController: LifecycleCameraController,
    owner: LifecycleOwner,
    onReceiveImage: (ImageProxy) -> Unit
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            PreviewView(it).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.BLACK)
                scaleType = PreviewView.ScaleType.FILL_START
                controller = camController
                camController.bindToLifecycle(owner)
            }
        })
    val context = LocalContext.current
    LaunchedEffect(true) {
        analyzeScreenImageRealtime(context, camController, onReceiveImage = onReceiveImage)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
//    CameraXTestScreen()
}