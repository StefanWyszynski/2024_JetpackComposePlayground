package com.jetpackcompose.playground.repos.data.repositiories

import com.jetpackcompose.playground.common.data.api.GitHubApiService
import com.jetpackcompose.playground.domain.repositories.GithubUsersRepository
import com.jetpackcompose.playground.users.data.dto.mapToDomain
import com.jetpackcompose.playground.users.domain.model.GithubUser
import com.jetpackcompose.playground.utils.NetworkOperation
import com.jetpackcompose.playground.utils.safeApiCallRunner
import javax.inject.Inject

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubUsersRepositoryImpl @Inject constructor(private var gitHubApiService: GitHubApiService) :
    GithubUsersRepository {

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