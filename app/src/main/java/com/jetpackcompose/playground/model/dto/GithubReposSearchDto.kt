package com.jetpackcompose.playground.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubReposSearchDto {
    @SerializedName("total_count")
    @Expose
    var totalCount: Int? = null

    @SerializedName("incomplete_results")
    @Expose
    var incompleteResults: Boolean? = null

    @SerializedName("items")
    @Expose
    var githubRepos: List<GithubRepoDto>? = null
}