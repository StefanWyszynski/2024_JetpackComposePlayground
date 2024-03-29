package com.jetpackcompose.playground.task_room.domain.usecase

import com.jetpackcompose.playground.task_room.domain.RealmTaskRepository
import com.jetpackcompose.playground.task_room.domain.data.RealmTask
import javax.inject.Inject

class DeleteRealmTaskUseCase @Inject constructor(var realmTaskRepository: RealmTaskRepository) {

    suspend fun deleteTask(realmTask: RealmTask) {
        realmTaskRepository.deleteTask(realmTask = realmTask)
    }
}