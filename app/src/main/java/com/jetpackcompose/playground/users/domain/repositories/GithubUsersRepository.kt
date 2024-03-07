package com.jetpackcompose.playground.domain.repositories

import com.jetpackcompose.playground.utils.NetworkOperation
import com.jetpackcompose.playground.users.domain.model.GithubUser

interface GithubUsersRepository {
    suspend fun searchUser(userName: String): NetworkOperation<List<GithubUser>>
}