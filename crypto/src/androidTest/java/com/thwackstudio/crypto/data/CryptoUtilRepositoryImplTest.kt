package com.thwackstudio.crypto.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.thwackstudio.crypto.data.util.CryptoBuilder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CryptoUtilRepositoryImplTest {

    lateinit var cryptoUtilRepository: CryptoUtilRepositoryImpl

    @Before
    fun setUp() {
        cryptoUtilRepository = CryptoUtilRepositoryImpl(CryptoBuilder())
    }

    @Test
    fun EncryptStringShouldReturnByteArrayAndDecryptShouldGiveTheSameResult() {
        val data = "secretData"
        val encryptedData = cryptoUtilRepository.encryptFromString(data)
        Truth.assertThat(encryptedData).isNotNull()
        val resultData = cryptoUtilRepository.decryptAsString(encryptedData!!)
        assert(resultData?.contentEquals(data) ?: false)
    }
}