package com.jetpackcompose.playground.users.presentation.redux

import com.jetpackcompose.playground.users.domain.model.GithubUser

sealed interface GithubUserState {
    data class Load(val userName: String) : GithubUserState
    data class Error(val e: Throwable, val userName: String) : GithubUserState
    data class ContentState(
        val users: List<GithubUser> = emptyList(),
        val userName: String = "",
    ) : GithubUserState
}