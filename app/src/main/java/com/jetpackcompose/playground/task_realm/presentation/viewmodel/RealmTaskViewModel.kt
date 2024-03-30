package com.jetpackcompose.playground.task_realm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.playground.task_room.domain.data.RealmTask
import com.jetpackcompose.playground.task_room.domain.usecase.DeleteRealmTaskUseCase
import com.jetpackcompose.playground.task_room.domain.usecase.GetAllRealmTasksUseCase
import com.jetpackcompose.playground.task_room.domain.usecase.InsertRealmTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RealmTaskViewModel @Inject constructor(
    private var deleteRealmTaskUseCase: DeleteRealmTaskUseCase,
    private var getAllRealmTasksUseCase: GetAllRealmTasksUseCase,
    private var insertRealmTaskUseCase: InsertRealmTaskUseCase
) : ViewModel() {

    private val _tasks: MutableStateFlow<List<RealmTask>> = MutableStateFlow(emptyList())
    val tasks: StateFlow<List<RealmTask>> = _tasks

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch() {
            getAllRealmTasksUseCase.getAllTasks()
                .flowOn(Dispatchers.IO)
                .collect {
                    _tasks.value = it
                }
        }
    }

    fun insertTask(realmTask: RealmTask) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                insertRealmTaskUseCase.insertTask(realmTask)
            }
        }
    }

    fun deleteTask(realmTask: RealmTask) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteRealmTaskUseCase.deleteTask(realmTask)
            }
        }
    }

}