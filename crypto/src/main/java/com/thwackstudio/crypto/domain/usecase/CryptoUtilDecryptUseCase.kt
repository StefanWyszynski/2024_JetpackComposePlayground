package com.thwackstudio.crypto.domain.usecase

import com.thwackstudio.crypto.domain.repository.CryptoUtilRepository
import javax.inject.Inject

class CryptoUtilDecryptUseCase @Inject constructor(val cryptoUtil: CryptoUtilRepository) {

    operator fun invoke(data: ByteArray): String? {
        return cryptoUtil.decryptAsString(data)
    }
}