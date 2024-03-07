package com.jetpackcompose.playground.repos.data.repositiories

import com.jetpackcompose.playground.utils.NetworkOperation
import com.jetpackcompose.playground.utils.safeApiCallRunner
import com.jetpackcompose.playground.common.data.api.GitHubApiService
import com.jetpackcompose.playground.domain.repositories.GithubUsersRepository
import com.jetpackcompose.playground.users.data.dto.mapToDomain
import com.jetpackcompose.playground.users.domain.model.GithubUser

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubUsersRepositoryImpl(private var gitHubApiService: GitHubApiService) : GithubUsersRepository {

    suspend override fun searchUser(userName: String): NetworkOperation<List<GithubUser>> {
        return safeApiCallRunner(
            apiCall = {
                gitHubApiService.searchUsers(userName)
            },
            onMapNetworkData = {
                it.mapToDomain()
            })
    }
}