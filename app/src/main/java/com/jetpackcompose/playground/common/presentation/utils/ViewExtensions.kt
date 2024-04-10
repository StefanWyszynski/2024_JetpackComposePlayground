package com.jetpackcompose.playground.common.presentation.utils

import androidx.compose.material3.DrawerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun topAppBarToogleVisibility(
    scope: CoroutineScope,
    drawerState: DrawerState
): () -> Unit = {
    scope.launch {
        drawerState.apply {
            if (isClosed) open() else close()
        }
    }
}