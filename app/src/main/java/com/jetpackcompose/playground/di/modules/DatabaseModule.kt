package com.jetpackcompose.playground.di.modules

import android.content.Context
import androidx.room.Room
import com.jetpackcompose.playground.main.data.database.AppDatabase
import com.jetpackcompose.playground.task_room.data.TaskDao
import com.jetpackcompose.playground.task_room.domain.data.RealmTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "room_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }

    @Provides
    @Singleton
    fun provideRealmDatabase(): Realm {
        val config = RealmConfiguration.create(schema = setOf(RealmTask::class))
        return Realm.open(config)
    }

}