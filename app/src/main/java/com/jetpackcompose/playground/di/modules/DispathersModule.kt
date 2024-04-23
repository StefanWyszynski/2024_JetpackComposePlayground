package com.jetpackcompose.playground.di.modules

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.jetpackcompose.playground.di.annotations.DispathersDefault
import com.jetpackcompose.playground.di.annotations.DispathersIO
import com.jetpackcompose.playground.main.data.api.GitHubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
}

