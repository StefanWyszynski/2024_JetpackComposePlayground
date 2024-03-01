package com.jetpackcompose.playground.common.network

import androidx.compose.runtime.Composable

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
sealed interface NetworkOperation<T> {

    data class Initial<T>(var placeholder: T? = null) : NetworkOperation<T>
    data class Loading<T>(var placeholder: T? = null) : NetworkOperation<T>

    data class Success<T>(var users: T) : NetworkOperation<T>
    data class Failure<T>(var reason: String? = null) : NetworkOperation<T>

    @Composable
    fun onLoading(action: @Composable (data: T?) -> Unit): NetworkOperation<T> {
        if (this is Loading) {
            action(this.placeholder)
        }
        return this
    }

    @Composable
    fun onSuccess(action: @Composable (data: T) -> Unit): NetworkOperation<T> {
        if (this is Success) {
            action(this.users)
        }
        return this
    }

    @Composable
    fun onFailure(action: @Composable (data: String?) -> Unit): NetworkOperation<T> {
        if (this is Failure) {
            action(this.reason)
        }
        return this
    }
}