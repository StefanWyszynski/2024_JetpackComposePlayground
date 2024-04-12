package com.jetpackcompose.playground.domain.repositories

import com.jetpackcompose.playground.users.domain.model.GithubUser
import com.jetpackcompose.playground.utils.NetworkOperation

interface GithubUsersRepository {
    suspend fun searchUser(userName: String): NetworkOperation<List<GithubUser>>
}