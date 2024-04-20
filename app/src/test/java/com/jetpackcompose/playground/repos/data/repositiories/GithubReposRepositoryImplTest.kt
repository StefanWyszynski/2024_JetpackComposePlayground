package com.jetpackcompose.playground.repos.data.repositiories

import com.google.common.truth.Truth
import com.google.gson.Gson
import com.jetpackcompose.playground.common.data.api.GitHubApiService
import com.jetpackcompose.playground.repos.data.dto.GithubRepoDto
import com.jetpackcompose.playground.repos.data.dto.GithubReposSearchDto
import com.jetpackcompose.playground.repos.data.dto.mapToDomain
import com.jetpackcompose.playground.utils.NetworkOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GithubReposRepositoryImplTest {

    lateinit var repository: GithubReposRepositoryImpl

    @Mock
    lateinit var gitHubApiService: GitHubApiService

    lateinit var mockWebServer: MockWebServer
    val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        gitHubApiService = retrofit.create(GitHubApiService::class.java)
        repository = GithubReposRepositoryImpl(gitHubApiService)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `searchRepos with some repo name should return success type class`() = runTest {
        val githubRepos = listOf(
            GithubRepoDto(name = "abc"),
            GithubRepoDto(name = "cde")
        )
        val users = GithubReposSearchDto(
            0, false, githubRepos
        )

        val usersJson = Gson().toJson(users)
        val mockResponse = MockResponse().setResponseCode(200).setBody(usersJson)
        mockWebServer.enqueue(mockResponse)

        val result = repository.searchRepos("test")

        val realResponse = NetworkOperation.Success(users.mapToDomain())
        Truth.assertThat(result).isInstanceOf(NetworkOperation.Success::class.java)
        Truth.assertThat(result).isEqualTo(realResponse)
    }

    @Test
    fun `searchRepos failure with normal users search`() = runTest {
        val mockResponse = MockResponse().setResponseCode(400).setBody("badRequest")
        mockWebServer.enqueue(mockResponse)

        val result = repository.searchRepos( "test")

        Truth.assertThat(result).isInstanceOf(NetworkOperation.Failure::class.java)
    }

    @Test
    fun `searchRepos with blank repo name should return Failure type class`() = runTest {
        val result = repository.searchRepos("")
        Truth.assertThat(result).isInstanceOf(NetworkOperation.Failure::class.java)
    }
}