package com.jetpackcompose.playground.crypto.domain.usecase

import com.jetpackcompose.playground.crypto.domain.repository.CryptoUtilRepository
import javax.inject.Inject

class CryptoUtilDecryptUseCase @Inject constructor(val cryptoUtil: CryptoUtilRepository) {

    operator fun invoke(data: ByteArray): String? {
        return cryptoUtil.decryptAsString(data)
    }
}