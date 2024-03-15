package com.jetpackcompose.playground.users.presentation.redux

sealed interface GithubUserAction {
    object Confirm : GithubUserAction
    object RetryLoadingUserAction : GithubUserAction
    data class TypeUserName(val userName: String) : GithubUserAction
}