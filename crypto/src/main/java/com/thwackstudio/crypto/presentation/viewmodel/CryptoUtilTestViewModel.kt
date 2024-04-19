package com.thwackstudio.crypto.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.thwackstudio.crypto.domain.usecase.CryptoUtilDecryptUseCase
import com.thwackstudio.crypto.domain.usecase.CryptoUtilEncryptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoUtilTestViewModel @Inject constructor(
    var cryptoUtilEncryptUseCase: CryptoUtilEncryptUseCase,
    var cryptoUtilDecryptUseCase: CryptoUtilDecryptUseCase
) : ViewModel() {
    fun encryptFromString(data: String): ByteArray? {
        return cryptoUtilEncryptUseCase(data)
    }

    fun decryptAsString(bytes: ByteArray): String? {
        return cryptoUtilDecryptUseCase(bytes)
    }

}