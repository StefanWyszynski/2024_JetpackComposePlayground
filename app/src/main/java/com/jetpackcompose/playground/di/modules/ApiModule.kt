package com.jetpackcompose.playground.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jetpackcompose.playground.common.data.api.GitHubApiService
import com.jetpackcompose.playground.domain.repositories.GithubUsersRepository
import com.jetpackcompose.playground.repos.data.repositiories.GithubReposRepositoryImpl
import com.jetpackcompose.playground.repos.data.repositiories.GithubUsersRepositoryImpl
import com.jetpackcompose.playground.repos.domain.repositories.GithubReposRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient()
            .serializeNulls()
//            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(HTTP_GITHUB_ROOT_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubApiService(retrofit: Retrofit): GitHubApiService {
        return retrofit.create(GitHubApiService::class.java)
    }


    companion object {
        val HTTP_GITHUB_ROOT_ENDPOINT = "https://api.github.com"
    }
}

