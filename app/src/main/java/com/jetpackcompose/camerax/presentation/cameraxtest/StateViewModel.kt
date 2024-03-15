package com.jetpackcompose.camerax.presentation.cameraxtest

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freeletics.flowredux.compose.rememberState
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
abstract class StateViewModel<State : Any, Action : Any>
@OptIn(ExperimentalCoroutinesApi::class) constructor(
    protected val stateMachine: FlowReduxStateMachine<State, Action>
) : ViewModel() {

    @Composable
    fun rememberState() = stateMachine.rememberState()

    fun dispatch(action: Action) = viewModelScope.launch {
        stateMachine.dispatch(action = action)
    }

}