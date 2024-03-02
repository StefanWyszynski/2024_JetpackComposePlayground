package com.jetpackcompose.playground.domain.use_case

import com.jetpackcompose.playground.common.network.NetworkOperation
import com.jetpackcompose.playground.domain.mappers.GithubUser
import com.jetpackcompose.playground.domain.repositories.GithubRepository
import javax.inject.Inject

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubSearchUserUseCase @Inject constructor(private val githubRepository: GithubRepository) {

    suspend operator fun invoke(userName: String): NetworkOperation<List<GithubUser>> {
        return githubRepository.searchUser(userName)
    }
}