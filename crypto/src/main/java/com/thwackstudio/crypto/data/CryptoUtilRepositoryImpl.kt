package com.thwackstudio.crypto.data

import com.thwackstudio.crypto.data.util.CryptoBuilder
import com.thwackstudio.crypto.domain.repository.CryptoUtilRepository
import javax.inject.Inject

class CryptoUtilRepositoryImpl @Inject constructor(val cryptoBuilder: CryptoBuilder) :
    CryptoUtilRepository {
    override fun encryptFromString(data: String): ByteArray? {
        return cryptoBuilder.build().encryptFromString(data)
    }

    override fun decryptAsString(data: ByteArray): String? {
        return cryptoBuilder.build().decryptAsString(data)
    }
}