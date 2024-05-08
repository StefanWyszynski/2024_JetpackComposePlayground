package com.jetpackcompose.playground.repos.data.dto

import com.google.gson.annotations.SerializedName
import com.jetpackcompose.playground.users.data.dto.GithubUserDto

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
data class GithubRepoDto(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("full_name")
    var fullName: String? = null,

    @SerializedName("private")
    var _private: Boolean? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null,

    @SerializedName("size")
    var size: Int? = null,

    @SerializedName("stargazers_count")
    var stargazersCount: Int? = null,

    @SerializedName("language")
    var language: String? = null,

    @SerializedName("has_wiki")
    var hasWiki: Boolean? = null,

    @SerializedName("archived")
    var archived: Boolean? = null,

    @SerializedName("score")
    var score: Double? = null,

    @SerializedName("owner")
    var owner: GithubUserDto? = null
)