package com.jetpackcompose.playground.ui.cameraxtest

import android.Manifest
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.jetpackcompose.playground.ui.common.components.MyTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraXTestScreen(scope: CoroutineScope, drawerState: DrawerState) {
    val permission: PermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)
    if (permission.hasPermission) {
        CameraXTestScreenContent(scope, drawerState)
    } else {
        NoPermissionScreen(permission, permission::launchPermissionRequest)
    }
}

@Composable
fun CameraXTestScreenContent(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val camController = remember {
        LifecycleCameraController(context)
    }
    Scaffold(
        topBar = {
            val topAppBarTitle = "CameraX test"
            MyTopAppBar(topAppBarTitle) {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                takePicture(context, camController)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "take image")
            }
        }) { scaffoldPading ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPading)
        )
        {
            CameraScreenContent(camController, owner)
        }
    }
}

private fun takePicture(context: Context, camController: LifecycleCameraController) {
    val mainExecutor = ContextCompat.getMainExecutor(context)
    camController.takePicture(mainExecutor, object :
        ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            image.close()
        }
    })
}

private fun analyzeScreenImageRealtime(context: Context, camController: LifecycleCameraController) {
    val mainExecutor = ContextCompat.getMainExecutor(context)
    camController.setImageAnalysisAnalyzer(mainExecutor, object : ImageAnalysis.Analyzer {
        override fun analyze(image: ImageProxy) {
            Log.e("TAG", "analyze: ${image.width}x${image.height}")
            image.close()
        }
    })
}

@Composable
fun CameraScreenContent(camController: LifecycleCameraController, owner: LifecycleOwner) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            PreviewView(it).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(android.graphics.Color.BLACK)
                scaleType = PreviewView.ScaleType.FILL_START
                controller = camController
                camController.bindToLifecycle(owner)
            }
        })
    val context = LocalContext.current
    LaunchedEffect(true) {
        analyzeScreenImageRealtime(
            context, camController
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
//    CameraXTestScreen()
}