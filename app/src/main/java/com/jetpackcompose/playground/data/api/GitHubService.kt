package com.jetpackcompose.playground.data.api

import com.jetpackcompose.playground.data.dto.GithubReposSearchDto
import com.jetpackcompose.playground.data.users.GithubUserDto
import com.jetpackcompose.playground.data.users.GithubUsersSearchDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
interface GitHubApiService {
    @Headers("Accept: application/vnd.github.mercy-preview+json")
    @GET("search/repositories")
    suspend fun searchRepos(@Query("q") term: String): Response<GithubReposSearchDto>

    @Headers("Accept: application/vnd.github.mercy-preview+json")
    @GET("search/users")
    suspend fun searchUsers(@Query("q") term: String): Response<GithubUsersSearchDto>

    @Headers("Accept: application/vnd.github.mercy-preview+json")
    @GET("users/{user}")
    suspend fun getUser(@Path("user") userName: String): Response<GithubUserDto>
}