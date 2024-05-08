package com.jetpackcompose.playground.repos.domain.use_case

import com.jetpackcompose.playground.repos.domain.model.GithubRepo
import com.jetpackcompose.playground.repos.domain.repositories.GithubReposRepository
import com.jetpackcompose.playground.utils.NetworkOperation
import javax.inject.Inject

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
class GithubSearchRepoUseCase @Inject constructor(private val githubRepository: GithubReposRepository) {
    suspend operator fun invoke(repoName: String): NetworkOperation<List<GithubRepo>> {
        return githubRepository.searchRepos(repoName)
    }

}