package com.jetpackcompose.playground.common.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freeletics.flowredux.compose.rememberState
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import kotlinx.coroutines.launch

abstract class StateViewModel<State : Any, Action : Any>(
    protected val stateMachine: FlowReduxStateMachine<State, Action>
) : ViewModel() {

    @Composable
    fun rememberState() = stateMachine.rememberState()

    fun dispatch(action: Action) = viewModelScope.launch {
        stateMachine.dispatch(action = action)
    }

}