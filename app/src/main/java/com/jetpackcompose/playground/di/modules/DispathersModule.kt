package com.jetpackcompose.playground.di.modules

import com.jetpackcompose.playground.di.annotations.DispathersDefault
import com.jetpackcompose.playground.di.annotations.DispathersIO
import com.jetpackcompose.playground.di.annotations.DispathersMain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/*
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@Module
@InstallIn(SingletonComponent::class)
class DispathersModule {

    @Provides
    @Singleton
    @DispathersIO
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    @DispathersDefault
    fun provideDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @Singleton
    @DispathersMain
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}

