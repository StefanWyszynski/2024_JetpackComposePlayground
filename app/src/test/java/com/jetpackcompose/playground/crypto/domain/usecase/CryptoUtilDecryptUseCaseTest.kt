package com.jetpackcompose.playground.crypto.domain.usecase

import com.jetpackcompose.playground.crypto.domain.repository.CryptoUtilRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CryptoUtilDecryptUseCaseTest {

    @Mock
    lateinit var cryptoUtil: CryptoUtilRepository

    @Test
    fun `decrypting ByteArray should return String`() {
        val encryptedData = "encryptedData".toByteArray()
        val decryptedString = "decryptedString"

        Mockito.`when`(cryptoUtil.decryptAsString(encryptedData)).thenReturn(decryptedString)

        val useCase = CryptoUtilDecryptUseCase(cryptoUtil)

        val result = useCase(encryptedData)

        assert(result == decryptedString)
    }
}