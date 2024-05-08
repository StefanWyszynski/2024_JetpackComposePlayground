package com.jetpackcompose.playground.task_realm.domain.usecase

import com.jetpackcompose.playground.task_realm.domain.RealmTaskRepository
import com.jetpackcompose.playground.task_realm.domain.data.RealmTask
import javax.inject.Inject

class InsertRealmTaskUseCase @Inject constructor(var realmTaskRepository: RealmTaskRepository) {

    suspend fun insertTask(realmTask: RealmTask) {
        realmTaskRepository.insertTask(realmTask = realmTask)
    }
}