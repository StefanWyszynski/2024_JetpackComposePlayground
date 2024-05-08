package com.jetpackcompose.playground.users.presentation.redux

sealed interface GithubUserAction {
    data object Confirm : GithubUserAction
    data object RetryLoadingUserAction : GithubUserAction
    data class TypeUserName(val userName: String) : GithubUserAction
}