package com.jetpackcompose.playground.task_realm.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.jetpackcompose.playground.main.presentation.data.TaskSubScreen
import com.jetpackcompose.playground.task_realm.domain.data.RealmTask
import com.jetpackcompose.playground.task_realm.presentation.components.RealmTaskListItem
import com.jetpackcompose.playground.task_realm.presentation.viewmodel.RealmTaskViewModel

@Composable
fun RealmTaskScreen(
    navController: NavHostController,
    taskViewModel: RealmTaskViewModel,
    customTopAppBarData: CustomTopAppBarData
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(customTopAppBarData)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                ScreenRoute.RealmTask(TaskSubScreen.TaskNew)
                    .navigate(navController, NavOptions.Builder())
            }, modifier = Modifier.testTag(TestConstants.REALM_TASK_LIST_ADD_NEW_TASK_BUTTON)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add task")
                Text(text = stringResource(R.string.add_task))
            }
        }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        )
        {
            val tasks by taskViewModel.tasks.collectAsStateWithLifecycle()
            SearchRepoScreenContent(tasks = tasks, onDelete = taskViewModel::deleteTask)
        }
    }
}

@Composable
private fun SearchRepoScreenContent(tasks: List<RealmTask>, onDelete: (RealmTask) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Tasks(tasks = tasks, onDelete = onDelete)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Tasks(tasks: List<RealmTask>, onDelete: (RealmTask) -> Unit) {
    val taskList = tasks.take(50)
    if (tasks.count() == 0) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = stringResource(R.string.no_tasks_found),
                modifier = Modifier
                    .padding(20.dp)
                    .testTag(TestConstants.REALM_TASK_LIST_NO_TASK_FOUND_TEXT)
            )
        }
    }
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(taskList.count()) { itemId ->
            val task = taskList[itemId]
            AnimatedVisibility(
                visible = true,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Box(modifier = Modifier.animateEnterExit(enter = scaleIn(), exit = scaleOut())) {
                    RealmTaskListItem(task, onItemClick = {

                    }, onDelete = {
                        onDelete(task)
                    })
                    HorizontalDivider()
                }
            }
        }
    }
}