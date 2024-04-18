package com.jetpackcompose.playground.task_room.data

import com.jetpackcompose.playground.task_room.domain.TaskRepository
import com.jetpackcompose.playground.task_room.domain.data.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    override suspend fun insertTask(task: Task) {
        return taskDao.insertTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        return taskDao.deleteTask(task)
    }
}