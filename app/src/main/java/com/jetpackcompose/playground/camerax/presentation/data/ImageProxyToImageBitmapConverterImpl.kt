package com.jetpackcompose.playground.camerax.presentation.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageProxyToImageBitmapConverterImpl @Inject constructor() :
    ImageProxyToImageBitmapConverter {

    suspend override fun convertImageAndClose(
        imageProxy: ImageProxy,
        dispatcher: CoroutineDispatcher
    ): ImageBitmap {
        try {
            return withContext(dispatcher) {
                convertToImageBitmap(imageProxy)
            }
        } finally {
            imageProxy.close()
        }
    }

    private fun convertToImageBitmap(imageProxy: ImageProxy): ImageBitmap {
        val buffer = imageProxy.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        return rotatedBitmap.asImageBitmap()
    }
}