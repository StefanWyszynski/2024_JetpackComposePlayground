package com.jetpackcompose.playground.task_room.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.task_room.domain.data.Task
import com.jetpackcompose.playground.task_room.domain.usecase.DeleteTaskUseCase
import com.jetpackcompose.playground.task_room.domain.usecase.GetAllTasksUseCase
import com.jetpackcompose.playground.task_room.domain.usecase.InsertTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    var deleteTaskUseCase: DeleteTaskUseCase,
    var getAllTasksUseCase: GetAllTasksUseCase,
    var insertTaskUseCase: InsertTaskUseCase
) : ViewModel() {

    private val _tasks: MutableStateFlow<List<Task>> = MutableStateFlow(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch() {
            getAllTasksUseCase.getAllTasks()
                .flowOn(Dispatchers.IO)
                .collect {
                    _tasks.value = it
                }
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                insertTaskUseCase.insertTask(task)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteTaskUseCase.deleteTask(task)
            }
        }
    }

}