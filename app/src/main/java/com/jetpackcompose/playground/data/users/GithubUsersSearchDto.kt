package com.jetpackcompose.playground.data.users

import com.google.gson.annotations.SerializedName
import com.jetpackcompose.playground.domain.mappers.GithubUser

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
data class GithubUsersSearchDto(
    @SerializedName("total_count")
    var totalCount: Int? = null,

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean? = null,

    @SerializedName("items")
    var githubUsers: List<GithubUserDto>? = null
)

fun GithubUsersSearchDto?.mapToDomain(): List<GithubUser> {
    return this?.githubUsers?.sortedBy { it.id }?.map {
        val receiver = GithubUser()
        with(receiver) {
            userName = it.login
            avatarUrl = it.avatarUrl
        }
        receiver
    } ?: arrayListOf()
}