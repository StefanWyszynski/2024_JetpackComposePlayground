package com.jetpackcompose.playground.repos.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.common.presentation.utils.DataStoreUtil
import com.jetpackcompose.playground.common.presentation.utils.DataStoreUtilProperty
import com.jetpackcompose.playground.repos.domain.model.GithubRepo
import com.jetpackcompose.playground.repos.domain.use_case.GithubSearchRepoUseCase
import com.jetpackcompose.playground.utils.NetworkOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class SearchRepoViewModel @Inject constructor(
    private val githubSearchRepoUseCase: GithubSearchRepoUseCase
) : ViewModel() {
    private var _searchText = MutableStateFlow("")
    var searchText: StateFlow<String> = _searchText.asStateFlow()

    private var _gitHubRepos =
        MutableStateFlow<NetworkOperation<List<GithubRepo>>>(NetworkOperation.Success(arrayListOf()))
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

    fun searchRepos(repoName: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch {
            _gitHubRepos.value = NetworkOperation.Loading()
            launch(dispatcher) {
                _gitHubRepos.value = githubSearchRepoUseCase(repoName)
            }
        }
    }
}