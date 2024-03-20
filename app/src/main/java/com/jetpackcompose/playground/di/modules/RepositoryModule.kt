package com.jetpackcompose.playground.di.modules

import com.jetpackcompose.playground.domain.repositories.GithubUsersRepository
import com.jetpackcompose.playground.repos.data.repositiories.GithubReposRepositoryImpl
import com.jetpackcompose.playground.repos.data.repositiories.GithubUsersRepositoryImpl
import com.jetpackcompose.playground.repos.domain.repositories.GithubReposRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGithubRepository(githubReposRepositoryImpl: GithubReposRepositoryImpl): GithubReposRepository

    @Binds
    abstract fun bindGraphQLResponseHandler(githubUsersRepositoryImpl: GithubUsersRepositoryImpl): GithubUsersRepository
}