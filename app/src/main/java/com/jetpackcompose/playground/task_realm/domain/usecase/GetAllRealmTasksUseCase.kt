package com.jetpackcompose.playground.task_realm.domain.usecase

import com.jetpackcompose.playground.task_realm.domain.RealmTaskRepository
import com.jetpackcompose.playground.task_realm.domain.data.RealmTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRealmTasksUseCase @Inject constructor(var realmTaskRepository: RealmTaskRepository) {

    suspend fun getAllTasks(): Flow<List<RealmTask>> {
        return realmTaskRepository.getAllTasks()
    }
}