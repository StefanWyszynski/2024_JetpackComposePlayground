package com.jetpackcompose.playground.crypto.domain.usecase

import com.jetpackcompose.playground.crypto.domain.repository.CryptoUtilRepository
import javax.inject.Inject

class CryptoUtilEncryptUseCase @Inject constructor(val cryptoUtil: CryptoUtilRepository) {

    operator fun invoke(data: String): ByteArray? {
        return cryptoUtil.encryptFromString(data)
    }
}