package com.jetpackcompose.playground.model.repositiories

import com.jetpackcompose.playground.domain.repositories.GithubRepository
import com.jetpackcompose.playground.model.api.GitHubApiService
import com.jetpackcompose.playground.model.dto.GithubReposSearchDto
import com.jetpackcompose.playground.model.users.GithubUsersSearchDto
import retrofit2.Response

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubRepositoryImpl constructor(private var gitHubApiService: GitHubApiService) :
    GithubRepository {

    suspend override fun searchUser(userName: String): Response<GithubUsersSearchDto> {
        return gitHubApiService.searchUsers(userName)
    }

    suspend override fun searchRepos(repoName: String): Response<GithubReposSearchDto> {
        return gitHubApiService.searchRepos(repoName)
    }
}