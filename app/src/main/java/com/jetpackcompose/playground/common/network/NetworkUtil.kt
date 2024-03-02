package com.jetpackcompose.playground.common.network

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
@Suppress("UNCHECKED_CAST")
suspend fun <T, R> safeApiCallRunner(
    apiCall: suspend () -> Response<T>,
    onMapNetworkData: ((T) -> R)? = null
): NetworkOperation<R> =
    try {
        val response = apiCall()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            withContext(Dispatchers.Main) {
                val bodyMapped = (onMapNetworkData?.invoke(body) ?: body) as? R
                if (bodyMapped != null) {
                    return@withContext NetworkOperation.Success(bodyMapped)
                } else {
                    return@withContext NetworkOperation.Failure("Response body class cast exception")
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                return@withContext NetworkOperation.Failure(response.errorBody()?.toString() ?: "")
            }
        }
    } catch (e: Exception) {
        if (e is CancellationException) throw e;
        withContext(Dispatchers.Main) {
            return@withContext NetworkOperation.Failure("" + e.toString())
        }
    }

