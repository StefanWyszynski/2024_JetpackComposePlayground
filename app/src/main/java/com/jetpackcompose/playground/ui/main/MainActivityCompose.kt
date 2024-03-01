package com.jetpackcompose.playground.ui.main

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jetpackcompose.playground.ui.search.repo.SearchRepoScreen
import com.jetpackcompose.playground.ui.search.user.SearchUserScreen
import com.jetpackcompose.playground.ui.theme.LearningAppTheme
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
    val currentScreenSate = remember {
        mutableStateOf(Screen.SearchRepo.route)
    }

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
        Scaffold(
            topBar = {
                val topAppBarTitle = if (currentScreenSate.value == Screen.SearchUser.route) {
                    "Search for user"
                } else {
                    "Search for repo"
                }
                TopAppBar(topAppBarTitle) {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            }) { scaffoldPading ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPading)
            )
            {
                NavHost(navController = navController, startDestination = Screen.SearchUser.route) {
                    composable(Screen.SearchUser.route) {
                        currentScreenSate.value = Screen.SearchUser.route
                        SearchUserScreen()
                    }
                    composable(Screen.SearchRepo.route) {
                        currentScreenSate.value = Screen.SearchRepo.route
                        SearchRepoScreen()
                    }
                }
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBar(title: String, onDrawerIconClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = onDrawerIconClick) {
                Icon(Icons.Filled.List, contentDescription = "")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearningAppTheme {
        val navController = rememberNavController()
        SetNavAppHost(navController)
    }
}