package com.jetpackcompose.playground.di.modules

import com.jetpackcompose.playground.domain.repositories.GithubUsersRepository
import com.jetpackcompose.playground.repos.data.repositiories.GithubReposRepositoryImpl
import com.jetpackcompose.playground.repos.data.repositiories.GithubUsersRepositoryImpl
import com.jetpackcompose.playground.repos.domain.repositories.GithubReposRepository
import com.jetpackcompose.playground.task_room.data.TaskRepositoryImpl
import com.jetpackcompose.playground.task_room.domain.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGithubRepository(githubReposRepositoryImpl: GithubReposRepositoryImpl): GithubReposRepository

    @Binds
    abstract fun bindGithubUserRepository(githubUsersRepositoryImpl: GithubUsersRepositoryImpl): GithubUsersRepository

    @Binds
    abstract fun bindTaskRepository(taskRepository: TaskRepositoryImpl): TaskRepository
}