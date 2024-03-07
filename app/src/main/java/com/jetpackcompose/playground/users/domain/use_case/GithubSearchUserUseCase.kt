package com.jetpackcompose.playground.domain.use_case

import com.jetpackcompose.playground.domain.repositories.GithubUsersRepository
import com.jetpackcompose.playground.utils.NetworkOperation
import com.jetpackcompose.playground.users.domain.model.GithubUser
import javax.inject.Inject

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubSearchUserUseCase @Inject constructor(private val githubRepository: GithubUsersRepository) {

    suspend operator fun invoke(userName: String): NetworkOperation<List<GithubUser>> {
        return githubRepository.searchUser(userName)
    }
}