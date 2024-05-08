package com.jetpackcompose.playground.di.modules

import com.jetpackcompose.playground.di.annotations.DispatchersDefault
import com.jetpackcompose.playground.di.annotations.DispatchersIO
import com.jetpackcompose.playground.di.annotations.DispatchersMain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {

    @Provides
    @Singleton
    @DispatchersIO
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    @DispatchersDefault
    fun provideDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @Singleton
    @DispatchersMain
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}

