package com.thwackstudio.crypto.domain.usecase

import com.thwackstudio.crypto.domain.repository.CryptoUtilRepository
import javax.inject.Inject

class CryptoUtilEncryptUseCase @Inject constructor(val cryptoUtil: CryptoUtilRepository) {

    operator fun invoke(data: String): ByteArray? {
        return cryptoUtil.encryptFromString(data)
    }
}