package com.jetpackcompose.playground.repos.domain.use_case

import com.google.common.truth.Truth
import com.jetpackcompose.playground.repos.domain.model.GithubRepo
import com.jetpackcompose.playground.repos.domain.repositories.GithubReposRepository
import com.jetpackcompose.playground.utils.NetworkOperation
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GithubSearchRepoUseCaseTest {

    @Mock
    private lateinit var githubRepository: GithubReposRepository
    private lateinit var githubSearchRepoUseCase: GithubSearchRepoUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        githubSearchRepoUseCase = GithubSearchRepoUseCase(githubRepository)
    }

    @Test
    fun `when searchRepos is invoked with valid repoName, it should return Success`() = runTest {
        val repoName = "example"
        val expectedResponse = NetworkOperation.Success(listOf(GithubRepo("Repo Name")))

        `when`(githubRepository.searchRepos(repoName)).thenReturn(expectedResponse)

        val result = githubSearchRepoUseCase(repoName)

        // Assert.assertEquals(expectedResponse, result)
        Truth.assertThat(result).isEqualTo(expectedResponse)
    }

    @Test
    fun `when searchRepos is invoked with invalid repoName, it should return Error`() = runTest {
        val repoName = ""
        val expectedResponse = NetworkOperation.Failure<List<GithubRepo>>("Invalid repo name")

        `when`(githubRepository.searchRepos(repoName)).thenReturn(expectedResponse)

        val result = githubSearchRepoUseCase(repoName)

        // Assert.assertEquals(result, expectedResponse)
        Truth.assertThat(result).isEqualTo(expectedResponse)
    }
}