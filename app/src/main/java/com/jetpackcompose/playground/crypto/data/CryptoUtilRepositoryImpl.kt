package com.jetpackcompose.playground.crypto.data

import com.jetpackcompose.playground.crypto.data.util.CryptoBuilder
import com.jetpackcompose.playground.crypto.domain.repository.CryptoUtilRepository
import javax.inject.Inject

class CryptoUtilRepositoryImpl @Inject constructor(val cryptoBuilder: CryptoBuilder) : CryptoUtilRepository {
    override fun encryptFromString(data: String): ByteArray? {
        return cryptoBuilder.build().encryptFromString(data)
    }

    override fun decryptAsString(data: ByteArray): String? {
        return cryptoBuilder.build().decryptAsString(data)
    }
}