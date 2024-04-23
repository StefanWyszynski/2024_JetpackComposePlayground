package com.jetpackcompose.playground.camerax.domain

import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ImageProxyToImageBitmapConverterUseCase
@Inject constructor(val imageProxyToImageBitmapConverter: ImageProxyToImageBitmapConverter) {

    suspend fun convertImageAndClose(
        imageProxy: ImageProxy,
        dispatcher: CoroutineDispatcher
    ): ImageBitmap? {
        return imageProxyToImageBitmapConverter.convertImageAndClose(imageProxy, dispatcher)
    }
}