package com.jetpackcompose.playground.repos.data.repositiories

import com.jetpackcompose.playground.utils.NetworkOperation
import com.jetpackcompose.playground.utils.safeApiCallRunner
import com.jetpackcompose.playground.common.data.api.GitHubApiService
import com.jetpackcompose.playground.repos.data.dto.mapToDomain
import com.jetpackcompose.playground.repos.domain.model.GithubRepo
import com.jetpackcompose.playground.repos.domain.repositories.GithubReposRepository

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubReposRepositoryImpl(private var gitHubApiService: GitHubApiService) : GithubReposRepository {

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