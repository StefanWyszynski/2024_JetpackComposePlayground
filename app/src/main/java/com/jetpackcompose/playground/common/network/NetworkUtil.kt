package com.jetpackcompose.playground.common.network

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
@Suppress("UNCHECKED_CAST")
fun <T, R> safeApiCallRunner(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> Response<T>,
    onMapNetworkData: ((T) -> R)? = null
): Flow<NetworkOperation<R>> = channelFlow {
    launch(dispatcher) {
        try {
            val response = apiCall()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                withContext(Dispatchers.Main) {
                    val bodyMapped = (onMapNetworkData?.invoke(body) ?: body) as? R
                    if (bodyMapped != null) {
                        send(NetworkOperation.Success(bodyMapped))
                    } else {
                        send(NetworkOperation.Failure("Class cast exception"))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    send(NetworkOperation.Failure(response.errorBody()?.toString() ?: ""))
                }
            }
        } catch (e: Exception) {
            send(NetworkOperation.Failure("" + e.toString()))
            if (e is CancellationException) throw e;
        }
    }
}
