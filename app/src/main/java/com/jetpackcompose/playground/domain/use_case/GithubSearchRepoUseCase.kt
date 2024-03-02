package com.jetpackcompose.playground.domain.use_case

import com.jetpackcompose.playground.common.network.NetworkOperation
import com.jetpackcompose.playground.domain.mappers.GithubRepo
import com.jetpackcompose.playground.domain.repositories.GithubRepository
import javax.inject.Inject

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubSearchRepoUseCase @Inject constructor(private val githubRepository: GithubRepository) {
    suspend operator fun invoke(repoName: String): NetworkOperation<List<GithubRepo>> {
        return githubRepository.searchRepos(repoName)
    }

}