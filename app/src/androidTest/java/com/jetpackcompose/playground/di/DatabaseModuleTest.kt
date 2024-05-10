package com.jetpackcompose.playground.di

import android.app.Application
import androidx.room.Room
import com.jetpackcompose.playground.main.data.database.AppDatabase
import com.jetpackcompose.playground.task_room.data.TaskDao
import com.jetpackcompose.playground.task_realm.domain.data.RealmTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

/*
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModuleTest {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }

    @Provides
    @Singleton
    fun provideRealmDatabase(): Realm {
        val config = RealmConfiguration.Builder(schema = setOf(RealmTask::class))
            .inMemory()
            .name("test-realm")
            .build()
        return Realm.open(config)
    }

}