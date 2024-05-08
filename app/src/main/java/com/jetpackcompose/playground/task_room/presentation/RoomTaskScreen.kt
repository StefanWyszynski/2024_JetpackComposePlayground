package com.jetpackcompose.playground.task_room.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.jetpackcompose.playground.R
import com.jetpackcompose.playground.common.presentation.components.CustomTopAppBar
import com.jetpackcompose.playground.common.presentation.utils.TestConstants
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.main.presentation.data.ScreenRoute
import com.jetpackcompose.playground.task_room.domain.data.Task
import com.jetpackcompose.playground.task_room.presentation.components.TaskListItem
import com.jetpackcompose.playground.task_room.presentation.viewmodel.TaskViewModel

@Composable
fun RoomTaskScreen(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    customTopAppBarData: CustomTopAppBarData
) {
    Scaffold(
        topBar = { CustomTopAppBar(customTopAppBarData) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                val nav = NavOptions.Builder().build()
                ScreenRoute.RoomTask.NewTask.navigate(navController, nav)
            }, modifier = Modifier.testTag(TestConstants.ROOM_TASK_LIST_ADD_NEW_TASK_BUTTON)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add task")
                Text(text = stringResource(R.string.add_task))
            }
        }) { scaffoldPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        )
        {
            val tasks by taskViewModel.tasks.collectAsStateWithLifecycle()
            SearchRepoScreenContent(
                tasks = tasks.take(50),
                onDeleteTask = taskViewModel::deleteTask
            )
        }
    }
}

@Composable
private fun SearchRepoScreenContent(tasks: List<Task>, onDeleteTask: (Task) -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Tasks(tasks = tasks, onDeleteTask = onDeleteTask)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Tasks(tasks: List<Task>, onDeleteTask: (Task) -> Unit) {
    if (tasks.count() == 0) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = stringResource(R.string.no_tasks_found),
                modifier = Modifier
                    .padding(20.dp)
                    .testTag(TestConstants.ROOM_TASK_LIST_NO_TASK_FOUND_TEXT)
            )
        }
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks.count()) { itemId ->
            val task = tasks[itemId]
            AnimatedVisibility(
                visible = true,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Box(modifier = Modifier.animateEnterExit(enter = scaleIn(), exit = scaleOut())) {
                    TaskListItem(task, onItemClick = {

                    }, onDelete = {
                        onDeleteTask(task)
                    })
                    HorizontalDivider()
                }
            }
        }
    }
}