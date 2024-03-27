package com.jetpackcompose.playground.task_room.domain.usecase

import com.jetpackcompose.playground.task_room.domain.TaskRepository
import com.jetpackcompose.playground.task_room.domain.data.Task
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(var taskRepository: TaskRepository) {

    suspend fun insertTask(task: Task) {
        taskRepository.insertTask(task = task)
    }
}