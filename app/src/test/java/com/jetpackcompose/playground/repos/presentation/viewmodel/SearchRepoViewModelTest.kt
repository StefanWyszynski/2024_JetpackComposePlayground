package com.jetpackcompose.playground.repos.presentation.viewmodel

import com.jetpackcompose.playground.repos.domain.model.GithubRepo
import com.jetpackcompose.playground.repos.domain.use_case.GithubSearchRepoUseCase
import com.jetpackcompose.playground.utils.NetworkOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SearchRepoViewModelTest {


    @Mock
    private lateinit var githubSearchRepoUseCase: GithubSearchRepoUseCase

    private lateinit var githubSearchViewModel: SearchRepoViewModel
    val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        githubSearchViewModel = SearchRepoViewModel(githubSearchRepoUseCase)
    }

    @Test
    fun `test searchRepos`() = runTest {
        // Given
        val repoName = "example"
        val repos = listOf(GithubRepo("ExampleRepo"))

        // When
        // mockk
//        coEvery { githubSearchRepoUseCase(repoName) } returns NetworkOperation.Success(repos)
        // testing mockito
        `when`(githubSearchRepoUseCase(repoName)).thenReturn(NetworkOperation.Success(repos))
        githubSearchViewModel.searchRepos(repoName, testDispatcher)

        // Then
        assertEquals(githubSearchViewModel.gitHubRepos.value, NetworkOperation.Success(repos))
    }

    @Test
    fun `test onSearchTextChange`() {
        val searchText = "example"

        githubSearchViewModel.onSearchTextChange(searchText)

        assertEquals(githubSearchViewModel.searchText.value, searchText)
    }
}