package com.jetpackcompose.playground.crypto.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.jetpackcompose.playground.crypto.domain.usecase.CryptoUtilDecryptUseCase
import com.jetpackcompose.playground.crypto.domain.usecase.CryptoUtilEncryptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoUtilTestViewModel @Inject constructor(private var cryptoUtilEncryptUseCase: CryptoUtilEncryptUseCase,
                                                  private var cryptoUtilDecryptUseCase: CryptoUtilDecryptUseCase
): ViewModel() {
    fun encryptFromString(data: String): ByteArray? {
        return cryptoUtilEncryptUseCase(data)
    }

    fun decryptAsString(bytes: ByteArray): String? {
        return cryptoUtilDecryptUseCase(bytes)
    }

}