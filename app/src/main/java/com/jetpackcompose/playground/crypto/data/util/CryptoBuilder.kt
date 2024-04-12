package com.jetpackcompose.playground.crypto.data.util

import com.jetpackcompose.playground.crypto.data.EncryptOptions


class CryptoBuilder() {

    private val encryptOptions = EncryptOptions()

    fun setAlgorithm(algorithm: String): CryptoBuilder {

        encryptOptions.algorithm = algorithm
//         val KEY_ALGORITHM_3DES = "DESede"
//         val KEY_ALGORITHM_AES = "AES"
//         val KEY_ALGORITHM_EC = "EC"
//         val KEY_ALGORITHM_HMAC_SHA1 = "HmacSHA1"
//         val KEY_ALGORITHM_HMAC_SHA224 = "HmacSHA224"
//         val KEY_ALGORITHM_HMAC_SHA256 = "HmacSHA256"
//         val KEY_ALGORITHM_HMAC_SHA384 = "HmacSHA384"
//         val KEY_ALGORITHM_HMAC_SHA512 = "HmacSHA512"
//         val KEY_ALGORITHM_RSA = "RSA"
        return this
    }

    fun setBlockMode(blockMode: String): CryptoBuilder {

        encryptOptions.blockMode = blockMode
//        val BLOCK_MODE_CBC = "CBC"
//        val BLOCK_MODE_CTR = "CTR"
//        val BLOCK_MODE_ECB = "ECB"
//        val BLOCK_MODE_GCM = "GCM"
        return this
    }

    fun setPadding(padding: String): CryptoBuilder {

        encryptOptions.padding = padding
//        val ENCRYPTION_PADDING_NONE = "NoPadding"
//        val ENCRYPTION_PADDING_PKCS7 = "PKCS7Padding"
//        val ENCRYPTION_PADDING_RSA_OAEP = "OAEPPadding"
//        val ENCRYPTION_PADDING_RSA_PKCS1 = "PKCS1Padding"
        return this
    }

    fun build() = CryptoUtil(encryptOptions)
}