package com.jetpackcompose.playground.users.domain.use_case

import com.jetpackcompose.playground.users.domain.model.GithubUser
import com.jetpackcompose.playground.users.domain.repositories.GithubUsersRepository
import com.jetpackcompose.playground.utils.NetworkOperation
import javax.inject.Inject

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
class GithubSearchUserUseCase @Inject constructor(private val githubRepository: GithubUsersRepository) {

    suspend operator fun invoke(userName: String): NetworkOperation<List<GithubUser>> {
        return githubRepository.searchUser(userName)
    }
}