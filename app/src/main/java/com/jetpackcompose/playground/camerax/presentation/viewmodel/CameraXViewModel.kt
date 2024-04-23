package com.jetpackcompose.playground.camerax.presentation.viewmodel

import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.camerax.domain.ImageProxyToImageBitmapConverterUseCase
import com.jetpackcompose.playground.di.annotations.DispathersDefault
import com.jetpackcompose.playground.utils.ThreadOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraXViewModel @Inject constructor(
    val imageProxyToImageBitmapConverterUseCase: ImageProxyToImageBitmapConverterUseCase,
    @DispathersDefault val defaultDispather: CoroutineDispatcher
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
                    imageProxyToImageBitmapConverterUseCase.convertImageAndClose(
                        image, defaultDispather
                    )
                processedImage?.let {
                    _receivedImageSharedFlow.emit(ThreadOperation.Success(processedImage))
                } ?: _receivedImageSharedFlow.emit(
                    ThreadOperation.Failure("Error occured while processing camera photo")
                )
            } finally {
                processingImage = false
            }
        }
    }

    fun deleteImage() {
        viewModelScope.launch { _receivedImageSharedFlow.emit(null) }
    }
}