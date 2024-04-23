package com.jetpackcompose.playground.repos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetpackcompose.playground.common.presentation.components.CustomTopAppBar
import com.jetpackcompose.playground.common.presentation.components.LoadingProgress
import com.jetpackcompose.playground.common.presentation.components.SearchField
import com.jetpackcompose.playground.common.presentation.components.SearchResultListItem
import com.jetpackcompose.playground.common.presentation.components.Spacer
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.repos.domain.model.GithubRepo
import com.jetpackcompose.playground.repos.presentation.viewmodel.SerachRepoViewModel
import com.jetpackcompose.playground.utils.NetworkOperation

/*
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@Composable
fun SearchRepoScreen(
    viewModel: SerachRepoViewModel,
    customTopAppBarData: CustomTopAppBarData
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(customTopAppBarData)
        }) { scaffoldPading ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPading)
        )
        {
            SearchRepoScreenContent(viewModel)
        }
    }
}

@Composable
private fun SearchRepoScreenContent(viewModel: SerachRepoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val searchText by viewModel.searchText.collectAsState("")
        val githubRepos by viewModel.gitHubRepos.collectAsStateWithLifecycle()
        SearchField(searchText, viewModel::onSearchTextChange)
        Spacer()
        ShowRepositories(repositiories = githubRepos)
    }
}

@Composable
private fun ShowRepositories(repositiories: NetworkOperation<List<GithubRepo>>) {
    repositiories.onSuccess { repos ->
        val reposRes = repos.take(50)
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(reposRes.count()) { itemId ->
                val repo = reposRes[itemId]
                SearchResultListItem(repo, repo.avatarUrl, repo.repoName, onItemClick = {

                })
                Divider()
            }
            if (repos.count() == 0) {
                item {
                    Text(
                        text = "No repos found", textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }.onLoading {
        LoadingProgress()
    }.onFailure {
        Text(
            text = "Something went wrong, " + (it ?: ""), textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}