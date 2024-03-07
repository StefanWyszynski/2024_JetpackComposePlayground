package com.jetpackcompose.playground.repos.domain.repositories

import com.jetpackcompose.playground.utils.NetworkOperation
import com.jetpackcompose.playground.repos.domain.model.GithubRepo

interface GithubReposRepository {
    suspend fun searchRepos(repoName: String): NetworkOperation<List<GithubRepo>>
}