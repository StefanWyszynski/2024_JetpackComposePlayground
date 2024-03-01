package com.jetpackcompose.playground.domain.use_case

import com.jetpackcompose.playground.common.network.NetworkOperation
import com.jetpackcompose.playground.common.network.safeApiCallRunner
import com.jetpackcompose.playground.domain.mappers.GithubRepo
import com.jetpackcompose.playground.model.dto.mapToDomain
import com.jetpackcompose.playground.model.repositiories.GithubRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubSearchRepoUseCase @Inject constructor(private val githubRepository: GithubRepositoryImpl) {
    operator fun invoke(repoName: String): Flow<NetworkOperation<List<GithubRepo>>> {
        return safeApiCallRunner(
            dispatcher = Dispatchers.IO,
            apiCall = {
                githubRepository.searchRepos(repoName)
            },
            onMapNetworkData = {
                it.mapToDomain()
            })
    }

}