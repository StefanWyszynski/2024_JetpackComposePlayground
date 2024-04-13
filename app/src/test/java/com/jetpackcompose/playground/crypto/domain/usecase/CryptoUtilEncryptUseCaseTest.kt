package com.jetpackcompose.playground.crypto.domain.usecase

import com.jetpackcompose.playground.crypto.domain.repository.CryptoUtilRepository
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
    lateinit var cryptoUtil: CryptoUtilRepository

    private lateinit var encryptUseCase: CryptoUtilEncryptUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        encryptUseCase = CryptoUtilEncryptUseCase(cryptoUtil)
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