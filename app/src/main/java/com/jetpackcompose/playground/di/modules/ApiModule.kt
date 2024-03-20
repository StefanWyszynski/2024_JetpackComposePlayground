package com.jetpackcompose.playground.di.modules

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.jetpackcompose.playground.common.data.api.GitHubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideApiLogger(gson: Gson): HttpLoggingInterceptor.Logger {
        return object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                val logName = "ApiLogger"
                if (message.startsWith("{") || message.startsWith("[")) {
                    try {
                        val prettyPrintJson = gson.toJson(JsonParser.parseString(message))
                        Log.d(logName, prettyPrintJson)
                    } catch (m: JsonSyntaxException) {
                        Log.d(logName, message)
                    }
                } else {
                    Log.d(logName, message)
                    return
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logger: HttpLoggingInterceptor.Logger): OkHttpClient {
        val logging = HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(HTTP_GITHUB_ROOT_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
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

