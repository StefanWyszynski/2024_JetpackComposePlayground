package com.jetpackcompose.playground.data.repositiories

import com.jetpackcompose.playground.common.network.NetworkOperation
import com.jetpackcompose.playground.common.network.safeApiCallRunner
import com.jetpackcompose.playground.data.api.GitHubApiService
import com.jetpackcompose.playground.data.dto.mapToDomain
import com.jetpackcompose.playground.data.users.mapToDomain
import com.jetpackcompose.playground.domain.mappers.GithubRepo
import com.jetpackcompose.playground.domain.mappers.GithubUser
import com.jetpackcompose.playground.domain.repositories.GithubRepository

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubRepositoryImpl(private var gitHubApiService: GitHubApiService) : GithubRepository {

    suspend override fun searchUser(userName: String): NetworkOperation<List<GithubUser>> {
        return safeApiCallRunner(
            apiCall = {
                gitHubApiService.searchUsers(userName)
            },
            onMapNetworkData = {
                it.mapToDomain()
            })
    }

    suspend override fun searchRepos(repoName: String): NetworkOperation<List<GithubRepo>> {
        return safeApiCallRunner(
            apiCall = {
                gitHubApiService.searchRepos(repoName)
            },
            onMapNetworkData = {
                it.mapToDomain()
            })
    }
}