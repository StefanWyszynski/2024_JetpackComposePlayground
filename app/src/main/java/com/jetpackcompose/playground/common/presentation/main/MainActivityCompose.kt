package com.jetpackcompose.playground.common.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.jetpackcompose.playground.common.presentation.data.NavigationDrawerItems
import com.jetpackcompose.playground.common.presentation.data.ScreenRoute
import com.jetpackcompose.playground.common.presentation.theme.JetpackComposePlaygroundAppTheme
import com.jetpackcompose.playground.common.presentation.utils.topAppBarToogleVisibility
import com.jetpackcompose.playground.compose_game_bench.presentation.GameScreen
import com.jetpackcompose.playground.compose_game_bench.presentation.viewmodel.GameViewModel
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/*
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@AndroidEntryPoint
class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposePlaygroundAppTheme {
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
fun SetNavAppHost(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val toppAppBarToogleCallback = remember { topAppBarToogleVisibility(scope, drawerState) }
    val customTopAppBarData by remember {
        derivedStateOf{
            CustomTopAppBarData(openIconClick = toppAppBarToogleCallback)
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerContent(navController) {
                drawerState.apply {
                    scope.launch {
                        close()
                    }
                }
            }

        },
        gesturesEnabled = drawerState.isOpen,
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
}

@Composable
fun ModalDrawerContent(navController: NavController, onClickOptionCallback: () -> Unit) {
    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .background(
                color = Color(0xFF000077),
                shape = RoundedCornerShape(corner = CornerSize(0.dp))
            ),
        drawerShape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color(0xFF000055),
                    shape = RoundedCornerShape(corner = CornerSize(0.dp))
                )
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = "Jetpack compose playground app",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(20.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
        val nav = NavOptions.Builder().setLaunchSingleTop(true).build()

        for (navigateItem in NavigationDrawerItems.entries) {
            DrawerContentOptionButton({
                navigateItem.navigate(navController, nav)
                onClickOptionCallback()
            }, stringResource(navigateItem.title))
        }
    }
}

@Composable
private fun DrawerContentOptionButton(
    onClickOption: () -> Unit, title: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovering by interactionSource.collectIsHoveredAsState()
    Button(modifier = Modifier
        .typicalButton(),
        colors = ButtonDefaults.buttonColors(
            contentColor = if (isHovering) Color(0xFFffffcc) else Color.White,
            containerColor = if (isHovering) Color(0xFFffffcc) else Color.White
        ),
        shape = RoundedCornerShape(4.dp),
        interactionSource = interactionSource,
        onClick = {
            onClickOption()
        }) {
        Text(text = title, color = Color.Black, fontSize = 10.sp)
    }
}

fun Modifier.typicalButton() = this
    .padding(4.dp)
    .fillMaxWidth(1f)
    .wrapContentHeight()

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposePlaygroundAppTheme {
        val navController = rememberNavController()
        SetNavAppHost(navController)
    }
}