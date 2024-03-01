package com.jetpackcompose.playground.model.users

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
class GithubUsersSearchDto {
    @SerializedName("total_count")
    @Expose
    var totalCount: Int? = null

    @SerializedName("incomplete_results")
    @Expose
    var incompleteResults: Boolean? = null

    @SerializedName("items")
    @Expose
    var githubUsers: List<GithubUserDto>? = null
}