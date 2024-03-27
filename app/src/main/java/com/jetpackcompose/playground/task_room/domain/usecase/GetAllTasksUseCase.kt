package com.jetpackcompose.playground.task_room.domain.usecase

import com.jetpackcompose.playground.task_room.domain.TaskRepository
import com.jetpackcompose.playground.task_room.domain.data.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(var taskRepository: TaskRepository) {

    suspend fun getAllTasks(): Flow<List<Task>> {
        return taskRepository.getAllTasks()
    }
}