package com.jetpackcompose.playground.task_room.presentation

import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jetpackcompose.playground.R
import com.jetpackcompose.playground.common.data.database.AppDatabase
import com.jetpackcompose.playground.common.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.common.presentation.data.ScreenRoute
import com.jetpackcompose.playground.common.presentation.main.MainActivityCompose
import com.jetpackcompose.playground.common.presentation.theme.JetpackComposePlaygroundAppTheme
import com.jetpackcompose.playground.common.presentation.utils.TestConstants
import com.jetpackcompose.playground.common.presentation.utils.topAppBarToogleVisibility
import com.jetpackcompose.playground.di.modules.DatabaseModule
import com.jetpackcompose.playground.task_room.data.TaskDao
import com.jetpackcompose.playground.task_room.presentation.viewmodel.TaskViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class TaskScreenKtTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivityCompose>()

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var taskDao: TaskDao

    @Before
    fun setUp() {
        hiltRule.inject()

        taskDao = appDatabase.taskDao()
        composeTestRule.activity.setContent {
            JetpackComposePlaygroundAppTheme {
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val toppAppBarToogleCallback =
                    remember { topAppBarToogleVisibility(scope, drawerState) }
                val customTopAppBarData =
                    CustomTopAppBarData(openIconClick = toppAppBarToogleCallback)
                NavHost(
                    navController = navController,
                    startDestination = ScreenRoute.Task.Main.getFullPath()
                ) {
                    composable(
                        route = ScreenRoute.Task.route,
                        arguments = ScreenRoute.Task.namedNavArguments()
                    ) { backStackEntry ->
                        val hiltViewModel = hiltViewModel<TaskViewModel>(backStackEntry)
                        val nestedScreen = backStackEntry.arguments?.getString("nestedScreen")
                        when (nestedScreen) {
                            ScreenRoute.Task.Main.route -> {
                                customTopAppBarData.title = stringResource(R.string.tasks)
                                TaskScreen(navController, hiltViewModel, customTopAppBarData)
                            }

                            ScreenRoute.Task.NewTask.route -> {
                                customTopAppBarData.title = stringResource(R.string.add_new_task)
                                NewTaskScreen(navController, hiltViewModel, customTopAppBarData)
                            }
                        }
                    }
                }

                val nav = NavOptions.Builder().setLaunchSingleTop(true).build()
                ScreenRoute.Task.Main.navigate(navController, nav = nav)
            }
        }
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun emptyTaskShouldDisplayNoTaskFoundText() {
        composeTestRule.apply {
            // wait until screen shows
            composeTestRule.waitUntilAtLeastOneExists(hasTestTag(TestConstants.ROOM_TASK_LIST_ADD_NEW_TASK_BUTTON))
            onNodeWithTag(TestConstants.ROOM_TASK_LIST_NO_TASK_FOUND_TEXT).assertIsDisplayed()
        }
    }

    @Test
    fun moveToAddTaskAndAddNewTaskShouldDisplayTaskInTasksScreen() {
        val text = "NewTaskTitle"
        composeTestRule.apply {
            // wait until screen shows
            waitUntilAtLeastOneExists(hasTestTag(TestConstants.ROOM_TASK_LIST_ADD_NEW_TASK_BUTTON))
            // click
            onNodeWithTag(TestConstants.ROOM_TASK_LIST_ADD_NEW_TASK_BUTTON).performClick()

            // move to add task screen
            waitUntilDoesNotExist(hasTestTag(TestConstants.ROOM_TASK_LIST_NO_TASK_FOUND_TEXT))

            onNodeWithTag(TestConstants.ROOM_ADD_TASK_TITLE).performTextInput(text)

            // go back
            onNodeWithTag(TestConstants.ROOM_ADD_TASK_ADD_BUTTON).performClick()

            waitUntilDoesNotExist(hasTestTag(TestConstants.ROOM_ADD_TASK_ADD_BUTTON))
            // check if task was added
            onNodeWithText(text).assertIsDisplayed()

            waitForIdle()
            onNodeWithTag(TestConstants.ROOM_TASK_LIST_NO_TASK_FOUND_TEXT).assertIsNotDisplayed()

            runTest {
                val tasks = taskDao.getAllTasks().first()
                if (tasks.isEmpty()) {
                    taskDao.deleteTask(tasks.first())
                }
            }
        }
    }

    @Test
    fun moveToAddTaskAndCancelShouldDisplayEmptyTaskList() {
        composeTestRule.apply {
            // wait until screen shows
            waitUntilAtLeastOneExists(hasTestTag(TestConstants.ROOM_TASK_LIST_ADD_NEW_TASK_BUTTON))
            // click
            onNodeWithTag(TestConstants.ROOM_TASK_LIST_ADD_NEW_TASK_BUTTON).performClick()

            // move to add task screen
            waitUntilDoesNotExist(hasTestTag(TestConstants.ROOM_TASK_LIST_NO_TASK_FOUND_TEXT))

            // go back
            onNodeWithTag(TestConstants.ROOM_ADD_TASK_CANCEL_BUTTON).performClick()

            waitUntilDoesNotExist(hasTestTag(TestConstants.ROOM_ADD_TASK_ADD_BUTTON))

            onNodeWithTag(TestConstants.ROOM_TASK_LIST_NO_TASK_FOUND_TEXT).assertIsDisplayed()
        }
    }
}