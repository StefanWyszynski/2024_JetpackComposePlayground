package com.jetpackcompose.playground.camerax.presentation.viewmodel

import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.camerax.presentation.data.ImageProxyToImageBitmapConverter
import com.jetpackcompose.playground.di.annotations.DispatchersDefault
import com.jetpackcompose.playground.utils.ThreadOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraXViewModel @Inject constructor(
    val imageProxyToImageBitmapConverterImpl: ImageProxyToImageBitmapConverter,
    @DispatchersDefault val defaultDispatcher: CoroutineDispatcher
) :
    ViewModel() {
    private var processingImage = false
    private val _receivedImageSharedFlow = MutableSharedFlow<ThreadOperation<ImageBitmap>?>(1)
    val receivedImageSharedFlow = _receivedImageSharedFlow.asSharedFlow()

    fun onReceiveImage(image: ImageProxy) {
        viewModelScope.launch {
            if (processingImage) {
                image.close()
                return@launch
            }
            _receivedImageSharedFlow.emit(ThreadOperation.Loading())
            try {
                processingImage = true
                val processedImage =
                    imageProxyToImageBitmapConverterImpl.convertImageAndClose(
                        image, defaultDispatcher
                    )
                processedImage.let {
                    _receivedImageSharedFlow.emit(ThreadOperation.Success(processedImage))
                }
            } catch (e: Exception) {
                _receivedImageSharedFlow.emit(
                    ThreadOperation.Failure("Error occurred while processing camera photo")
                )
                if (e is CancellationException) throw e
            } finally {
                processingImage = false
            }
        }
    }

    fun deleteImage() {
        viewModelScope.launch { _receivedImageSharedFlow.emit(null) }
    }
}