package com.jetpackcompose.playground.ui.search.repo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.common.network.NetworkOperation
import com.jetpackcompose.playground.domain.mappers.GithubRepo
import com.jetpackcompose.playground.domain.use_case.GithubSearchRepoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
@HiltViewModel
class SerachRepoViewModel @Inject constructor(
    private val githubSearchRepoUseCase: GithubSearchRepoUseCase,
) : ViewModel() {

    private var _searchText = MutableStateFlow("")
    var searchText: StateFlow<String> = _searchText.asStateFlow()

    private var _gitHubRepos =
        MutableStateFlow<NetworkOperation<List<GithubRepo>>>(NetworkOperation.Initial())
    var gitHubRepos = _gitHubRepos.asStateFlow()

    init {
        viewModelScope.launch {
            val searchTextHot = searchText.stateIn(viewModelScope)
            searchTextHot
                .debounce(500)
                .collect { text ->
                    if (!text.isBlank()) {
                        searchRepos(text)
                    }
                }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun searchRepos(repoName: String) {
        viewModelScope.launch {
            _gitHubRepos.value = NetworkOperation.Loading()
            githubSearchRepoUseCase(repoName).collect {
                _gitHubRepos.value = it
            }
        }
    }
}