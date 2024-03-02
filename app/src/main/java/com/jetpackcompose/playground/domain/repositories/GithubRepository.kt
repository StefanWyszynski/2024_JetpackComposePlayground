package com.jetpackcompose.playground.domain.repositories

import com.jetpackcompose.playground.common.network.NetworkOperation
import com.jetpackcompose.playground.domain.mappers.GithubRepo
import com.jetpackcompose.playground.domain.mappers.GithubUser

interface GithubRepository {
    suspend fun searchUser(userName: String): NetworkOperation<List<GithubUser>>
    suspend fun searchRepos(repoName: String): NetworkOperation<List<GithubRepo>>
}