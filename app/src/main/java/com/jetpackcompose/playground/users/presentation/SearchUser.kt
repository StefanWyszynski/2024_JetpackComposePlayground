package com.jetpackcompose.playground.users.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetpackcompose.playground.common.presentation.components.LoadingProgress
import com.jetpackcompose.playground.common.presentation.components.MyTopAppBar
import com.jetpackcompose.playground.common.presentation.components.SearchResultListItem
import com.jetpackcompose.playground.common.presentation.components.searchField
import com.jetpackcompose.playground.common.presentation.components.spacer
import com.jetpackcompose.playground.users.domain.model.GithubUser
import com.jetpackcompose.playground.users.presentation.viewmodel.SerachUserViewModel
import com.jetpackcompose.playground.utils.NetworkOperation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
@Composable
fun SearchUserScreen(
    viewModel: SerachUserViewModel, scope: CoroutineScope, drawerState: DrawerState
) {
    Scaffold(
        topBar = {
            val topAppBarTitle = "Search for user"
            MyTopAppBar(topAppBarTitle) {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
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
        val serachText by viewModel.searchText.collectAsState("")
        val gitHubUsers by viewModel.gitHubUsers.collectAsStateWithLifecycle()
        searchField(serachText, viewModel::onSearchTextChange)
        spacer()
        showUsers(users = gitHubUsers)
    }
}

@Composable
private fun showUsers(users: NetworkOperation<List<GithubUser>>) {
    users.onSuccess { users ->
        val reposRes = users.take(50)
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(reposRes.count()) { itemId ->
                val repo = reposRes[itemId]
                SearchResultListItem(repo, repo.avatarUrl, repo.userName, onItemClick = {

                })
                Divider()
            }
        }
    }.onLoading {
        LoadingProgress()
    }.onFailure {
        Text(text = "Something went wrong" + (it ?: ""))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
//    SearchUserScreen(viewModel = viewModel())
}