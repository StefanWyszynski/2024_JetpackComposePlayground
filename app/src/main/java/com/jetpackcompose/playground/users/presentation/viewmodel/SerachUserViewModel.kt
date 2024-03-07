package com.jetpackcompose.playground.users.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.domain.use_case.GithubSearchUserUseCase
import com.jetpackcompose.playground.utils.NetworkOperation
import com.jetpackcompose.playground.users.domain.model.GithubUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
@OptIn(FlowPreview::class)
@HiltViewModel
class SerachUserViewModel @Inject constructor(
    private val githubSearchUserUseCase: GithubSearchUserUseCase
) : ViewModel() {

    private var _searchText = MutableStateFlow("")
    var searchText: StateFlow<String> = _searchText.asStateFlow()

    private var _gitHubUsers =
        MutableStateFlow<NetworkOperation<List<GithubUser>>>(NetworkOperation.Initial())
    var gitHubUsers = _gitHubUsers.asStateFlow()

    init {
        viewModelScope.launch {
            val searchTextHot = searchText.stateIn(viewModelScope)
            searchTextHot
                .debounce(500)
                .collect { text ->
                    if (!text.isBlank()) {
                        searchUsers(text)
                    }
                }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun searchUsers(userName: String) {
        viewModelScope.launch {
            _gitHubUsers.value = NetworkOperation.Loading()

            launch(Dispatchers.IO) {
                _gitHubUsers.value = githubSearchUserUseCase(userName)
            }
        }
    }
}