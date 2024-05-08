package com.jetpackcompose.playground.task_realm.domain

import com.jetpackcompose.playground.task_realm.domain.data.RealmTask
import kotlinx.coroutines.flow.Flow

interface RealmTaskRepository {
    suspend fun getAllTasks(): Flow<List<RealmTask>>
    suspend fun insertTask(realmTask: RealmTask)
    suspend fun deleteTask(realmTask: RealmTask)
}