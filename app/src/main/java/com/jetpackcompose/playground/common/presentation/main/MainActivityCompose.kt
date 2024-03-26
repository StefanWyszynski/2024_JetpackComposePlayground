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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.jetpackcompose.camerax.presentation.cameraxtest.CameraXTestScreen
import com.jetpackcompose.playground.repos.presentation.SearchRepoScreen
import com.jetpackcompose.playground.repos.presentation.viewmodel.SerachRepoViewModel
import com.jetpackcompose.playground.users.presentation.SearchUserScreen
import com.jetpackcompose.playground.users.presentation.viewmodel.SerachUserViewModel
import com.jetpackcompose.playground.common.presentation.theme.LearningAppTheme
import com.jetpackcompose.playground.maps.presentation.GoogleMapScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/*
 * Copyright 2023
 *
 * @author Stefan Wyszynski
 */
sealed class Screen(val route: String) {
    object SearchUser : Screen("searchUser")
    object SearchRepo : Screen("searchRepo")
    object CameraXTest : Screen("CameraXTest")
    object MapsTest : Screen("MapTest")
}

@AndroidEntryPoint
class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningAppTheme {
                // A surface container using the 'background' color from the theme
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
        modifier = Modifier
            .background(Color.Black)
    ) {
        NavHost(navController = navController, startDestination = Screen.SearchUser.route) {
            composable(Screen.SearchUser.route) {
                val hiltViewModel = hiltViewModel<SerachUserViewModel>(it)
                SearchUserScreen(hiltViewModel, scope, drawerState)
            }
            composable(Screen.SearchRepo.route) {
                val hiltViewModel = hiltViewModel<SerachRepoViewModel>(it)
                SearchRepoScreen(hiltViewModel, scope, drawerState)
            }
            composable(Screen.CameraXTest.route) {
                CameraXTestScreen(scope, drawerState)
            }
            composable(Screen.MapsTest.route) {
                GoogleMapScreen(scope, drawerState)
            }

        }
    }
}


@Composable
fun DrawerContent(
    navController: NavController, onClickOption: () -> Unit,
) {
    Text(
        text = "Test app",
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(20.dp),
        fontWeight = FontWeight.Bold
    )
    val nav = NavOptions.Builder().setLaunchSingleTop(true).build()

    DrawerContentOptionButton({
        GotoSearchUsers(navController, nav)
        onClickOption()
    }, "Search users")

    DrawerContentOptionButton({
        GotoSearchRepos(navController, nav)
        onClickOption()
    }, "Search repos")
    DrawerContentOptionButton({
        GotoCameraXTest(navController, nav)
        onClickOption()
    }, "Camera X test")
    DrawerContentOptionButton({
        GotoCameraXTest(navController, nav)
        onClickOption()
    }, "Map test")
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
    navController.navigate(Screen.SearchUser.route, navOptions = nav)
}

private fun GotoSearchRepos(navController: NavController, nav: NavOptions) {
    navController.navigate(Screen.SearchRepo.route, navOptions = nav)
}

private fun GotoCameraXTest(navController: NavController, nav: NavOptions) {
    navController.navigate(Screen.CameraXTest.route, navOptions = nav)
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearningAppTheme {
        val navController = rememberNavController()
        SetNavAppHost(navController)
    }
}