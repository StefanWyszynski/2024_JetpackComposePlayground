package com.jetpackcompose.playground.camerax.domain

import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.CoroutineDispatcher

interface ImageProxyToImageBitmapConverter {

    suspend fun convertImageAndClose(
        imageProxy: ImageProxy,
        dispatcher: CoroutineDispatcher
    ): ImageBitmap?
}