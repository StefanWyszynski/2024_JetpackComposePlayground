package com.thwackstudio.crypto.data.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.thwackstudio.crypto.data.EncryptOptions
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

open class CryptoUtil(private val encryptOptions: EncryptOptions) {

    private val transformation: String

    init {
        transformation = encryptOptions.getTransformation()
    }

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher = Cipher.getInstance(transformation).apply {
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    fun encrypt(bytes: ByteArray): ByteArray? {
        val encBytes = encryptCipher.doFinal(bytes)
        val outputStream = ByteArrayOutputStream()
        outputStream.use {
            it.write(encryptCipher.iv.size)
            it.write(encryptCipher.iv)
            it.write(encBytes.size)
            it.write(encBytes)
        }
        return outputStream.toByteArray()
    }

    fun decrypt(bytes: ByteArray): ByteArray? {
        val inputStream = ByteArrayInputStream(bytes)
        return inputStream.use {
            val ivSize = it.read()
            val bytesIv = ByteArray(ivSize)
            it.read(bytesIv)

            val encryptedBytes = it.read()
            val encBytes = ByteArray(encryptedBytes)
            it.read(encBytes)

            getDecryptCipherForIv(bytesIv).doFinal(encBytes)
        }
    }

    fun encryptFromString(data: String): ByteArray? {
        return encrypt(data.encodeToByteArray())
    }

    fun decryptAsString(bytes: ByteArray): String? {
        return decrypt(bytes)?.decodeToString()
    }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(transformation).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        val key = keyStore.getEntry(SECRET, null) as? KeyStore.SecretKeyEntry
        return key?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(encryptOptions.algorithm).apply {
            init(
                KeyGenParameterSpec.Builder(
                    SECRET, KeyProperties.PURPOSE_ENCRYPT or
                            KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(encryptOptions.blockMode)
                    .setEncryptionPaddings(encryptOptions.padding)
                    .setUserAuthenticationRequired(encryptOptions.userAuthenticationRequired)
                    .setRandomizedEncryptionRequired(encryptOptions.randomizedEncryptionRequired)
                    .build()
            )
        }.generateKey()
    }

    companion object {
        private const val SECRET = "secret"
    }
}
