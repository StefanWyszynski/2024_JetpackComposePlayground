package com.jetpackcompose.playground.task_realm.presentation

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
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.main.presentation.data.ScreenRoute
import com.jetpackcompose.playground.main.presentation.main.MainActivityCompose
import com.jetpackcompose.playground.main.presentation.theme.JetpackComposePlaygroundAppTheme
import com.jetpackcompose.playground.common.presentation.utils.TestConstants
import com.jetpackcompose.playground.common.presentation.utils.topAppBarToogleVisibility
import com.jetpackcompose.playground.di.modules.DatabaseModule
import com.jetpackcompose.playground.task_realm.data.RealmTaskRepositoryImpl
import com.jetpackcompose.playground.task_realm.presentation.viewmodel.RealmTaskViewModel
import com.jetpackcompose.playground.task_room.presentation.RealmNewTaskScreen
import com.jetpackcompose.playground.task_room.presentation.RealmTaskScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.realm.kotlin.Realm
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
class RealmTaskScreenKtTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivityCompose>()

    @Inject
    lateinit var realm: Realm

    private lateinit var repository: RealmTaskRepositoryImpl

    @Before
    fun setUp() {
        hiltRule.inject()

        repository = RealmTaskRepositoryImpl(realm)
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
                    startDestination = ScreenRoute.RealmTask.Main.getFullPath()
                ) {
                    composable(
                        route = ScreenRoute.RealmTask.route,
                        arguments = ScreenRoute.RealmTask.namedNavArguments()
                    ) { backStackEntry ->
                        val hiltViewModel = hiltViewModel<RealmTaskViewModel>(backStackEntry)
                        val nestedScreen = backStackEntry.arguments?.getString("nestedScreen")
                        when (nestedScreen) {
                            ScreenRoute.RealmTask.Main.route -> {
                                customTopAppBarData.title = stringResource(R.string.tasks)
                                RealmTaskScreen(
                                    navController,
                                    hiltViewModel,
                                    customTopAppBarData
                                )
                            }

                            ScreenRoute.RealmTask.NewTask.route -> {
                                customTopAppBarData.title =
                                    stringResource(R.string.add_new_task)
                                RealmNewTaskScreen(
                                    navController,
                                    hiltViewModel,
                                    customTopAppBarData
                                )
                            }

                        }
                    }
                }
                val nav = NavOptions.Builder().setLaunchSingleTop(true).build()
                ScreenRoute.RealmTask.Main.navigate(navController, nav = nav)
            }
        }
    }

    @After
    fun tearDown() {
        realm.close()
    }

    @Test
    fun emptyTaskShouldDisplayNoTaskFoundText() {
        composeTestRule.apply {
            // wait until screen shows
            composeTestRule.waitUntilAtLeastOneExists(hasTestTag(TestConstants.REALM_TASK_LIST_ADD_NEW_TASK_BUTTON))
            onNodeWithTag(TestConstants.REALM_TASK_LIST_NO_TASK_FOUND_TEXT).assertIsDisplayed()
        }
    }

    @Test
    fun moveToAddTaskAndAddNewTaskShouldDisplayTaskInTasksScreen() {
        val text = "NewTaskTitle"
        composeTestRule.apply {
            // wait until screen shows
            composeTestRule.waitUntilAtLeastOneExists(hasTestTag(TestConstants.REALM_TASK_LIST_ADD_NEW_TASK_BUTTON))
            // click
            onNodeWithTag(TestConstants.REALM_TASK_LIST_ADD_NEW_TASK_BUTTON).performClick()

            // move to add task screen
            composeTestRule.waitUntilDoesNotExist(hasTestTag(TestConstants.REALM_TASK_LIST_NO_TASK_FOUND_TEXT))

            onNodeWithTag(TestConstants.REALM_ADD_TASK_TITLE).performTextInput(text)

            // go back
            onNodeWithTag(TestConstants.REALM_ADD_TASK_ADD_BUTTON).performClick()

            composeTestRule.waitUntilDoesNotExist(hasTestTag(TestConstants.REALM_ADD_TASK_ADD_BUTTON))
            // check if task was added
            onNodeWithText(text).assertIsDisplayed()

            waitForIdle()
            onNodeWithTag(TestConstants.REALM_TASK_LIST_NO_TASK_FOUND_TEXT).assertIsNotDisplayed()

            runTest {
                val tasks = repository.getAllTasks().first()
                if (tasks.isEmpty()) {
                    repository.deleteTask(tasks.first())
                }
            }
        }
    }

    @Test
    fun moveToAddTaskAndCancelShouldDisplayEmptyTaskList() {
        composeTestRule.apply {
            // wait until screen shows
            composeTestRule.waitUntilAtLeastOneExists(hasTestTag(TestConstants.REALM_TASK_LIST_ADD_NEW_TASK_BUTTON))
            // click
            onNodeWithTag(TestConstants.REALM_TASK_LIST_ADD_NEW_TASK_BUTTON).performClick()

            // move to add task screen
            composeTestRule.waitUntilDoesNotExist(hasTestTag(TestConstants.REALM_TASK_LIST_NO_TASK_FOUND_TEXT))

            // go back
            onNodeWithTag(TestConstants.REALM_ADD_TASK_CANCEL_BUTTON).performClick()

            composeTestRule.waitUntilDoesNotExist(hasTestTag(TestConstants.REALM_ADD_TASK_ADD_BUTTON))

            onNodeWithTag(TestConstants.REALM_TASK_LIST_NO_TASK_FOUND_TEXT).assertIsDisplayed()
        }
    }
}