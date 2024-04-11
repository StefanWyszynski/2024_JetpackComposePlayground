package com.jetpackcompose.playground.crypto.domain.repository

interface CryptoUtilRepository {
    fun encryptFromString(data: String): ByteArray?
    fun decryptAsString(data: ByteArray): String?
}