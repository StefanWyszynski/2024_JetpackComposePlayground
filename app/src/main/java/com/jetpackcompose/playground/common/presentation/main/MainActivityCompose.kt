package com.jetpackcompose.playground.common.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jetpackcompose.playground.R
import com.jetpackcompose.playground.camerax.presentation.cameraxtest.CameraXScreenContainer
import com.jetpackcompose.playground.common.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.common.presentation.data.ScreenRoute
import com.jetpackcompose.playground.common.presentation.theme.LearningAppTheme
import com.jetpackcompose.playground.common.presentation.utils.topAppBarToogleVisibility
import com.jetpackcompose.playground.compose_game_bench.presentation.GameScreen
import com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel.GameViewModel
import com.jetpackcompose.playground.maps.presentation.GoogleMapScreen
import com.jetpackcompose.playground.repos.presentation.SearchRepoScreen
import com.jetpackcompose.playground.repos.presentation.viewmodel.SerachRepoViewModel
import com.jetpackcompose.playground.task_realm.presentation.viewmodel.RealmTaskViewModel
import com.jetpackcompose.playground.task_room.presentation.NewRealmTaskScreen
import com.jetpackcompose.playground.task_room.presentation.NewTaskScreen
import com.jetpackcompose.playground.task_room.presentation.RealmTaskScreen
import com.jetpackcompose.playground.task_room.presentation.TaskScreen
import com.jetpackcompose.playground.task_room.presentation.viewmodel.TaskViewModel
import com.jetpackcompose.playground.users.presentation.SearchUserScreen
import com.jetpackcompose.playground.users.presentation.viewmodel.SerachUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
@AndroidEntryPoint
class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    contentColor = Color.Blue
                ) {
                    val navController = rememberNavController()
                    SetNavAppHost(navController)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetNavAppHost(
    navController: NavHostController
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val toppAppBarToogleCallback = remember { topAppBarToogleVisibility(scope, drawerState) }
    val customTopAppBarData = CustomTopAppBarData(openIconClick = toppAppBarToogleCallback)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(navController) {
                    drawerState.apply {
                        scope.launch {
                            close()
                        }
                    }
                }
            }
        },
        gesturesEnabled = drawerState.isOpen,
        modifier = Modifier
            .background(Color.Black)
    ) {
        NavHost(navController = navController, startDestination = ScreenRoute.SearchUser.route) {
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
                CameraXScreenContainer(customTopAppBarData)
            }
            composable(ScreenRoute.MapsTest.route) {
                GoogleMapScreen()
            }
            composable(
                route = ScreenRoute.Task.route, arguments = ScreenRoute.Task.namedNavArguments()
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
                        customTopAppBarData.title = stringResource(R.string.add_new_task)
                        NewRealmTaskScreen(navController, hiltViewModel, customTopAppBarData)
                    }

                }
            }

        }
    }
}

@Composable
fun DrawerContent(navController: NavController, onClickOptionCallback: () -> Unit) {
    Text(
        text = "Jetpack compose playground app",
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(20.dp),
        fontWeight = FontWeight.Bold
    )
    val nav = NavOptions.Builder().setLaunchSingleTop(true).build()

    DrawerContentOptionButton({
        GotoGameScreen(navController, nav)
        onClickOptionCallback()
    }, "3D game - drawing playground")

    DrawerContentOptionButton({
        GotoSearchUsers(navController, nav)
        onClickOptionCallback()
    }, "Search github users")

    DrawerContentOptionButton({
        GotoSearchRepos(navController, nav)
        onClickOptionCallback()
    }, "Search github repos")

    DrawerContentOptionButton({
        GotoCameraXTest(navController, nav)
        onClickOptionCallback()
    }, "Camera X test")

//    DrawerContentOptionButton({
//        GotoMapsTest(navController, nav)
//        onClickOptionCallback()
//    }, "Map test")

    DrawerContentOptionButton({
        GotoTaskTest(navController, nav)
        onClickOptionCallback()
    }, "Task")

    DrawerContentOptionButton({
        GotoRealmTaskTest(navController, nav)
        onClickOptionCallback()
    }, "Realm Task")
}

@Composable
private fun DrawerContentOptionButton(
    onClickOption: () -> Unit, title: String
) {
    Button(modifier = Modifier.typicalButton(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Blue,
            containerColor = Color.White
        ),
        onClick = {
            onClickOption()
        }) {
        Text(text = title, color = Color.Black)
    }
}

fun Modifier.typicalButton() = this
    .fillMaxWidth(1f)
    .wrapContentHeight()
    .padding(8.dp)
    .clip(RoundedCornerShape(20.dp))

private fun GotoSearchUsers(navController: NavController, nav: NavOptions) {
    navController.navigate(ScreenRoute.SearchUser.route, navOptions = nav)
}

private fun GotoSearchRepos(navController: NavController, nav: NavOptions) {
    navController.navigate(ScreenRoute.SearchRepo.route, navOptions = nav)
}

private fun GotoCameraXTest(navController: NavController, nav: NavOptions) {
    navController.navigate(ScreenRoute.CameraXTest.route, navOptions = nav)
}

private fun GotoMapsTest(navController: NavController, nav: NavOptions) {
    navController.navigate(ScreenRoute.MapsTest.route, navOptions = nav)
}

private fun GotoGameScreen(navController: NavController, nav: NavOptions) {
    navController.navigate(ScreenRoute.GameScreen.route, navOptions = nav)
}

private fun GotoTaskTest(navController: NavController, nav: NavOptions) {
    ScreenRoute.Task.Main.navigate(navController, nav)
}

private fun GotoRealmTaskTest(navController: NavController, nav: NavOptions) {
    ScreenRoute.RealmTask.Main.navigate(navController, nav)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearningAppTheme {
        val navController = rememberNavController()
        SetNavAppHost(navController)
    }
}