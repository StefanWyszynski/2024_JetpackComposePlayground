package com.jetpackcompose.playground.main.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jetpackcompose.playground.R
import com.jetpackcompose.playground.camerax.presentation.CameraXScreenContainer
import com.jetpackcompose.playground.camerax.presentation.viewmodel.CameraXViewModel
import com.jetpackcompose.playground.compose_game_bench.presentation.GameScreen
import com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel.GameViewModel
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.main.presentation.data.ScreenRoute
import com.jetpackcompose.playground.maps.presentation.GoogleMapScreen
import com.jetpackcompose.playground.repos.presentation.SearchRepoScreen
import com.jetpackcompose.playground.repos.presentation.viewmodel.SerachRepoViewModel
import com.jetpackcompose.playground.task_realm.presentation.viewmodel.RealmTaskViewModel
import com.jetpackcompose.playground.task_room.presentation.RealmNewTaskScreen
import com.jetpackcompose.playground.task_room.presentation.RealmTaskScreen
import com.jetpackcompose.playground.task_room.presentation.RoomNewTaskScreen
import com.jetpackcompose.playground.task_room.presentation.RoomTaskScreen
import com.jetpackcompose.playground.task_room.presentation.viewmodel.TaskViewModel
import com.jetpackcompose.playground.users.presentation.SearchUserScreen
import com.jetpackcompose.playground.users.presentation.viewmodel.SerachUserViewModel
import com.thwackstudio.crypto.presentation.CryptoUtilTestScreen
import com.thwackstudio.crypto.presentation.viewmodel.CryptoUtilTestViewModel


@Composable
fun MainContent(
    navController: NavHostController,
    customTopAppBarData: CustomTopAppBarData
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.SearchUser.route
    ) {
        composable(ScreenRoute.GameScreen.route) {
            val hiltViewModel = hiltViewModel<GameViewModel>(it)
            GameScreen(hiltViewModel)
        }
        composable(ScreenRoute.SearchUser.route) {
            customTopAppBarData.title = stringResource(R.string.search_for_user)
            val hiltViewModel = hiltViewModel<SerachUserViewModel>(it)
            SearchUserScreen(hiltViewModel, customTopAppBarData)
        }
        composable(ScreenRoute.SearchRepo.route) {
            customTopAppBarData.title = stringResource(R.string.search_for_repo)
            val hiltViewModel = hiltViewModel<SerachRepoViewModel>(it)
            SearchRepoScreen(hiltViewModel, customTopAppBarData)
        }
        composable(ScreenRoute.CameraXTest.route) {
            customTopAppBarData.title = stringResource(R.string.camerax_test)
            val hiltViewModel = hiltViewModel<CameraXViewModel>(it)
            CameraXScreenContainer(customTopAppBarData, hiltViewModel)
        }
        composable(ScreenRoute.MapsTest.route) {
            GoogleMapScreen()
        }
        composable(
            route = ScreenRoute.RoomTask.route,
            arguments = ScreenRoute.RoomTask.namedNavArguments()
        ) { backStackEntry ->
            val hiltViewModel = hiltViewModel<TaskViewModel>(backStackEntry)
            val nestedScreen = backStackEntry.arguments?.getString("nestedScreen")
            when (nestedScreen) {
                ScreenRoute.RoomTask.Main.route -> {
                    customTopAppBarData.title = stringResource(R.string.tasks)
                    RoomTaskScreen(navController, hiltViewModel, customTopAppBarData)
                }

                ScreenRoute.RoomTask.NewTask.route -> {
                    customTopAppBarData.title = stringResource(R.string.add_task)
                    RoomNewTaskScreen(navController, hiltViewModel, customTopAppBarData)
                }

            }
        }
        composable(
            route = ScreenRoute.RealmTask.route,
            arguments = ScreenRoute.RealmTask.namedNavArguments()
        ) { backStackEntry ->
            val hiltViewModel = hiltViewModel<RealmTaskViewModel>(backStackEntry)
            val nestedScreen = backStackEntry.arguments?.getString("nestedScreen")
            when (nestedScreen) {
                ScreenRoute.RealmTask.Main.route -> {
                    customTopAppBarData.title = stringResource(R.string.tasks)
                    RealmTaskScreen(navController, hiltViewModel, customTopAppBarData)
                }

                ScreenRoute.RealmTask.NewTask.route -> {
                    customTopAppBarData.title = stringResource(R.string.add_task)
                    RealmNewTaskScreen(navController, hiltViewModel, customTopAppBarData)
                }

            }
        }

        composable(ScreenRoute.CryptoUtilTest.route) {
            val hiltViewModel = hiltViewModel<CryptoUtilTestViewModel>(it)
            CryptoUtilTestScreen(hiltViewModel)
        }
    }
}