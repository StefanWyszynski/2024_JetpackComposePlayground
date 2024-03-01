package com.jetpackcompose.playground.domain.use_case

import com.jetpackcompose.playground.common.network.NetworkOperation
import com.jetpackcompose.playground.common.network.safeApiCallRunner
import com.jetpackcompose.playground.domain.mappers.GithubUser
import com.jetpackcompose.playground.model.repositiories.GithubRepositoryImpl
import com.jetpackcompose.playground.model.users.mapToDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubSearchUserUseCase @Inject constructor(private val githubRepository: GithubRepositoryImpl) {

    operator fun invoke(userName: String): Flow<NetworkOperation<List<GithubUser>>> {
        return safeApiCallRunner(
            dispatcher = Dispatchers.IO,
            apiCall = {
                githubRepository.searchUser(userName)
            },
            onMapNetworkData = {
                it.mapToDomain()
            })
    }
}