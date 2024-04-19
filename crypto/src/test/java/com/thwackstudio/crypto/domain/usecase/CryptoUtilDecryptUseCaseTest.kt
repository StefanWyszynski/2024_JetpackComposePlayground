package com.thwackstudio.crypto.domain.usecase

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CryptoUtilDecryptUseCaseTest {

    @Mock
    lateinit var cryptoUtil: com.thwackstudio.crypto.domain.repository.CryptoUtilRepository

    @Test
    fun `decrypting ByteArray should return String`() {
        val encryptedData = "encryptedData".toByteArray()
        val decryptedString = "decryptedString"

        Mockito.`when`(cryptoUtil.decryptAsString(encryptedData)).thenReturn(decryptedString)

        val useCase = com.thwackstudio.crypto.domain.usecase.CryptoUtilDecryptUseCase(cryptoUtil)

        val result = useCase(encryptedData)

        assert(result == decryptedString)
    }
}