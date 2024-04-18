package com.jetpackcompose.playground.users.data.dto

import com.google.gson.annotations.SerializedName
import com.jetpackcompose.playground.users.domain.model.GithubUser

/*
 * Copyright 2024
 *
 * @author Stefan Wyszyński
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