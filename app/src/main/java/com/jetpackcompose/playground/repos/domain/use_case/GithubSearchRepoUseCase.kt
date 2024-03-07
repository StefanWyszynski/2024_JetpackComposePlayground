package com.jetpackcompose.playground.repos.domain.use_case

import com.jetpackcompose.playground.utils.NetworkOperation
import com.jetpackcompose.playground.repos.domain.model.GithubRepo
import com.jetpackcompose.playground.repos.domain.repositories.GithubReposRepository
import javax.inject.Inject

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubSearchRepoUseCase @Inject constructor(private val githubRepository: GithubReposRepository) {
    suspend operator fun invoke(repoName: String): NetworkOperation<List<GithubRepo>> {
        return githubRepository.searchRepos(repoName)
    }

}