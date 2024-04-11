package com.jetpackcompose.playground.crypto.data

import com.jetpackcompose.playground.crypto.data.util.CryptoBuilder
import com.jetpackcompose.playground.crypto.domain.repository.CryptoUtilRepository
import javax.inject.Inject

class CryptoUtilRepositoryImpl @Inject constructor(): CryptoUtilRepository
{
    override fun encryptFromString(data: String): ByteArray? {
        return CryptoBuilder().build().encryptFromString(data)
    }

    override fun decryptAsString(data: ByteArray): String? {
        return CryptoBuilder().build().decryptAsString(data)
    }
}