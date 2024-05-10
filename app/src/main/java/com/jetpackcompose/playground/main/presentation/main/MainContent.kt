package com.jetpackcompose.playground.main.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jetpackcompose.playground.R
import com.jetpackcompose.playground.camerax.presentation.CameraXScreenContainer
import com.jetpackcompose.playground.camerax.presentation.viewmodel.CameraXViewModel
import com.jetpackcompose.playground.compose_game_bench.presentation.GameScreen
import com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel.GameViewModel
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.main.presentation.data.ScreenRoute
import com.jetpackcompose.playground.maps.presentation.GoogleMapScreen
import com.jetpackcompose.playground.repos.presentation.SearchRepoScreen
import com.jetpackcompose.playground.repos.presentation.viewmodel.SearchRepoViewModel
import com.jetpackcompose.playground.task_realm.presentation.RealmNewTaskScreen
import com.jetpackcompose.playground.task_realm.presentation.RealmTaskScreen
import com.jetpackcompose.playground.task_realm.presentation.viewmodel.RealmTaskViewModel
import com.jetpackcompose.playground.task_room.presentation.RoomNewTaskScreen
import com.jetpackcompose.playground.task_room.presentation.RoomTaskScreen
import com.jetpackcompose.playground.task_room.presentation.viewmodel.TaskViewModel
import com.jetpackcompose.playground.users.presentation.SearchUserScreen
import com.jetpackcompose.playground.users.presentation.viewmodel.SearchUserViewModel
import com.thwackstudio.crypto.presentation.CryptoUtilTestScreen
import com.thwackstudio.crypto.presentation.viewmodel.CryptoUtilTestViewModel

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@Composable
fun MainContent(
    navController: NavHostController,
    customTopAppBarData: CustomTopAppBarData
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.SearchUser
    ) {
        composable<ScreenRoute.GameScreen> {
            val hiltViewModel = hiltViewModel<GameViewModel>(it)
            GameScreen(hiltViewModel)
        }
        composable<ScreenRoute.SearchUser> {
            customTopAppBarData.title = stringResource(R.string.search_for_user)
            val hiltViewModel = hiltViewModel<SearchUserViewModel>(it)
            SearchUserScreen(hiltViewModel, customTopAppBarData)
        }
        composable<ScreenRoute.SearchRepo> {
            customTopAppBarData.title = stringResource(R.string.search_for_repo)
            val hiltViewModel = hiltViewModel<SearchRepoViewModel>(it)
            SearchRepoScreen(hiltViewModel, customTopAppBarData)
        }
        composable<ScreenRoute.CameraXTest> {
            customTopAppBarData.title = stringResource(R.string.camerax_test)
            val hiltViewModel = hiltViewModel<CameraXViewModel>(it)
            CameraXScreenContainer(customTopAppBarData, hiltViewModel)
        }
        composable<ScreenRoute.MapsTest> {
            GoogleMapScreen()
        }
        composable<ScreenRoute.RoomTask> { backStackEntry ->
            val hiltViewModel = hiltViewModel<TaskViewModel>(backStackEntry)
            val nestedScreen = backStackEntry.toRoute<ScreenRoute.RoomTask>().nastedScreen
            when (nestedScreen) {
                0 -> {
                    customTopAppBarData.title = stringResource(R.string.tasks)
                    RoomTaskScreen(navController, hiltViewModel, customTopAppBarData)
                }

                1 -> {
                    customTopAppBarData.title = stringResource(R.string.add_task)
                    RoomNewTaskScreen(navController, hiltViewModel, customTopAppBarData)
                }

                else -> {}

            }
        }
        composable<ScreenRoute.RealmTask> { backStackEntry ->
            val hiltViewModel = hiltViewModel<RealmTaskViewModel>(backStackEntry)
            val nestedScreen = backStackEntry.toRoute<ScreenRoute.RealmTask>().nastedScreen
            when (nestedScreen) {
                0 -> {
                    customTopAppBarData.title = stringResource(R.string.tasks)
                    RealmTaskScreen(navController, hiltViewModel, customTopAppBarData)
                }

                1 -> {
                    customTopAppBarData.title = stringResource(R.string.add_task)
                    RealmNewTaskScreen(navController, hiltViewModel, customTopAppBarData)
                }

                else -> {}
            }
        }

        composable<ScreenRoute.CryptoUtilTest> {
            val hiltViewModel = hiltViewModel<CryptoUtilTestViewModel>(it)
            CryptoUtilTestScreen(hiltViewModel)
        }
    }
}