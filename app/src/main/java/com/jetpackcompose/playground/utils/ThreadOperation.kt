package com.jetpackcompose.playground.utils

/*
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
sealed interface ThreadOperation<T> {

    data class Loading<T>(var placeholder: T? = null) : ThreadOperation<T>

    data class Success<T>(var data: T) : ThreadOperation<T>
    data class Failure<T>(var reason: String? = null) : ThreadOperation<T>
}