package com.jetpackcompose.playground.ui.search.repo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpackcompose.playground.common.network.NetworkOperation
import com.jetpackcompose.playground.domain.mappers.GithubRepo
import com.jetpackcompose.playground.ui.search.common.components.LoadingProgress
import com.jetpackcompose.playground.ui.search.common.components.SearchResultListItem
import com.jetpackcompose.playground.ui.search.common.components.searchField
import com.jetpackcompose.playground.ui.search.common.components.spacer
import com.jetpackcompose.playground.ui.search.repo.viewmodel.SerachRepoViewModel

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
@Composable
fun SearchRepoScreen(viewModel: SerachRepoViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val serachText by viewModel.searchText.collectAsState("")
        val githubRepos by viewModel.gitHubRepos.collectAsStateWithLifecycle()
        searchField(serachText, viewModel::onSearchTextChange)
        spacer()
        showRepositories(repositiories = githubRepos)
    }
}

@Composable
private fun showRepositories(repositiories: NetworkOperation<List<GithubRepo>>) {
    repositiories.onSuccess { repos ->
        val reposRes = repos.take(50)
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(reposRes.count()) { itemId ->
                val repo = reposRes[itemId]
                SearchResultListItem(repo, repo.avatarUrl, repo.repoName, onItemClick = {

                })
                Divider()
            }
        }
    }.onLoading {
        LoadingProgress()
    }.onFailure {
        Text(text = "Something went wrong, " + (it ?: ""))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SearchRepoScreen(viewModel = viewModel())
}