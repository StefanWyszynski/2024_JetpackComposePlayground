package com.thwackstudio.crypto.data

import android.security.keystore.KeyProperties

data class EncryptOptions(
    var algorithm: String = KeyProperties.KEY_ALGORITHM_AES,
    var blockMode: String = KeyProperties.BLOCK_MODE_CBC,
    var padding: String = KeyProperties.ENCRYPTION_PADDING_PKCS7,
    var userAuthenticationRequired: Boolean = false,
    var randomizedEncryptionRequired: Boolean = true
) {
    fun getTransformation(): String {
        return "$algorithm/$blockMode/$padding"
    }
}