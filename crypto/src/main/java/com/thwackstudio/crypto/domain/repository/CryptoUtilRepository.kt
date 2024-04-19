package com.thwackstudio.crypto.domain.repository

interface CryptoUtilRepository {
    fun encryptFromString(data: String): ByteArray?
    fun decryptAsString(data: ByteArray): String?
}