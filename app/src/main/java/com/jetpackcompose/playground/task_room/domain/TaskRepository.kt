package com.jetpackcompose.playground.task_room.domain

import com.jetpackcompose.playground.task_room.domain.data.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getAllTasks(): Flow<List<Task>>
    suspend fun insertTask(task: Task)
    suspend fun deleteTask(task: Task)
}