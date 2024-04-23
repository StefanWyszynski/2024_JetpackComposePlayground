package com.jetpackcompose.playground.users.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetpackcompose.playground.common.presentation.components.CustomTopAppBar
import com.jetpackcompose.playground.common.presentation.components.LoadingProgress
import com.jetpackcompose.playground.common.presentation.components.SearchField
import com.jetpackcompose.playground.common.presentation.components.SearchResultListItem
import com.jetpackcompose.playground.common.presentation.components.Spacer
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.users.presentation.redux.GithubUserState
import com.jetpackcompose.playground.users.presentation.viewmodel.SerachUserViewModel

/*
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@Composable
fun SearchUserScreen(
    viewModel: SerachUserViewModel, customTopAppBarData: CustomTopAppBarData
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
            SearchUserScreenContent(viewModel)
        }
    }
}

@Composable
private fun SearchUserScreenContent(viewModel: SerachUserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val searchText by viewModel.searchText.collectAsState("")
        val gitHubUsers by viewModel.gitHubUsers.collectAsStateWithLifecycle()
        SearchField(searchText, viewModel::onSearchTextChange)
        Spacer()
        ShowUsers(gitHubUsers, viewModel::onErrorAction)
    }
}

@Composable
private fun ShowUsers(githubUserState: GithubUserState?, onErrorAction: () -> Unit) {
    when (githubUserState) {
        is GithubUserState.ContentState -> {
            val reposRes = githubUserState.users.take(50)
            if (reposRes.size > 0) {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(reposRes.count()) { itemId ->
                        val repo = reposRes[itemId]
                        SearchResultListItem(repo, repo.avatarUrl, repo.userName, onItemClick = {
                            // next screen here
                        })
                        Divider()
                    }
                }
            } else {
                Text(
                    text = "No users found",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        is GithubUserState.Error -> {
            Text(
                text = "Something went wrong" + (githubUserState.e.localizedMessage ?: ""),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = onErrorAction) {
                Text(text = "Retry")
            }
        }

        is GithubUserState.Load -> {
            LoadingProgress()
        }

        null -> Unit
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
//    SearchUserScreen(viewModel = viewModel())
}