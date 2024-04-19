package com.thwackstudio.crypto.domain.usecase

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CryptoUtilEncryptUseCaseTest {

    @Mock
    lateinit var cryptoUtil: com.thwackstudio.crypto.domain.repository.CryptoUtilRepository

    private lateinit var encryptUseCase: com.thwackstudio.crypto.domain.usecase.CryptoUtilEncryptUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        encryptUseCase = com.thwackstudio.crypto.domain.usecase.CryptoUtilEncryptUseCase(cryptoUtil)
    }

    @Test
    fun `encrypting String should return ByteArray`() {
        val data = "encryptedData"
        val encryptedData = byteArrayOf(1, 2, 3)

        Mockito.`when`(cryptoUtil.encryptFromString(data)).thenReturn(encryptedData)

        val result = encryptUseCase(data)

        assert(result?.contentEquals(encryptedData) ?: false)
    }
}