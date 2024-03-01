package com.jetpackcompose.playground.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jetpackcompose.playground.domain.mappers.GithubRepo
import com.jetpackcompose.playground.model.users.GithubUserDto

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubRepoDto {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("full_name")
    @Expose
    var fullName: String? = null

    @SerializedName("private")
    @Expose
    var _private: Boolean? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("size")
    @Expose
    var size: Int? = null

    @SerializedName("stargazers_count")
    @Expose
    var stargazersCount: Int? = null

    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("has_wiki")
    @Expose
    var hasWiki: Boolean? = null

    @SerializedName("archived")
    @Expose
    var archived: Boolean? = null

    @SerializedName("score")
    @Expose
    var score: Double? = null

    @SerializedName("owner")
    @Expose
    var owner: GithubUserDto? = null
}

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