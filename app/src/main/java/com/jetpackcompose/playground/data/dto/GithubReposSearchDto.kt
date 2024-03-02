package com.jetpackcompose.playground.data.dto

import com.google.gson.annotations.SerializedName
import com.jetpackcompose.playground.domain.mappers.GithubRepo

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
data class GithubReposSearchDto(
    @SerializedName("total_count")
    var totalCount: Int? = null,

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean? = null,

    @SerializedName("items")
    var githubRepos: List<GithubRepoDto>? = null
)

fun GithubReposSearchDto?.mapToDomain(): List<GithubRepo> {
    return this?.githubRepos?.sortedBy { it.id }?.map {
        val receiver = GithubRepo()
        with(receiver) {
            userName = it.owner?.login ?: it.fullName
            avatarUrl = it.owner?.avatarUrl
            repoName = it.name
        }
        receiver
    } ?: arrayListOf()
}