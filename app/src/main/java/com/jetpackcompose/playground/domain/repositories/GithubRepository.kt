package com.jetpackcompose.playground.domain.repositories

import com.jetpackcompose.playground.model.dto.GithubReposSearchDto
import com.jetpackcompose.playground.model.users.GithubUsersSearchDto
import retrofit2.Response

interface GithubRepository {
    suspend fun searchUser(userName: String): Response<GithubUsersSearchDto>
    suspend fun searchRepos(repoName: String): Response<GithubReposSearchDto>
}