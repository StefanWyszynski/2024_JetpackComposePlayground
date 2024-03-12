package com.jetpackcompose.playground.users.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.jetpackcompose.camerax.presentation.cameraxtest.StateViewModel
import com.jetpackcompose.playground.domain.use_case.GithubSearchUserUseCase
import com.jetpackcompose.playground.utils.NetworkOperation
import com.jetpackcompose.playground.users.domain.model.GithubUser
import com.jetpackcompose.playground.users.presentation.redux.GithubUserAction
import com.jetpackcompose.playground.users.presentation.redux.GithubUserState
import com.jetpackcompose.playground.users.presentation.redux.GithubUsersStateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SerachUserViewModel @Inject constructor(
    private val githubUsersStateMachine: GithubUsersStateMachine
) : StateViewModel<GithubUserState, GithubUserAction>(githubUsersStateMachine) {

    private var _searchText = MutableStateFlow("")
    var searchText: StateFlow<String> = _searchText.asStateFlow()

    private var _gitHubUsers = MutableStateFlow<GithubUserState>(GithubUserState.ContentState())
    var gitHubUsers = _gitHubUsers.asStateFlow()

    init {
        viewModelScope.launch {
            val searchTextHot = _searchText.stateIn(viewModelScope)
            searchTextHot
                .debounce(500)
                .collect { text ->
                    if (!text.isBlank()) {
                        dispatch(GithubUserAction.Confirm)
                    }
                }
        }

        viewModelScope.launch {
            stateMachine.state.collect { newState ->
                _gitHubUsers.value = newState
            }
        }
    }

    fun onSearchTextChange(text: String) {
        dispatch(GithubUserAction.TypeUserName(text))
        _searchText.value = text
    }

    fun onErrorAction() {
        dispatch(GithubUserAction.RetryLoadingUserAction)
    }
}