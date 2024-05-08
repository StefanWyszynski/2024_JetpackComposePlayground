package com.jetpackcompose.playground.main.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jetpackcompose.playground.common.presentation.utils.topAppBarToggleVisibility
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData
import com.jetpackcompose.playground.main.presentation.data.ScreenRoute
import com.jetpackcompose.playground.main.presentation.theme.JetpackComposePlaygroundAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
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

    val topAppBarToggleCallback = remember { topAppBarToggleVisibility(scope, drawerState) }
    val customTopAppBarData by remember {
        derivedStateOf {
            CustomTopAppBarData(openIconClick = topAppBarToggleCallback)
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController) {
                drawerState.apply {
                    scope.launch {
                        close()
                    }
                }
            }
        },
        gesturesEnabled = !isGameOpened(navController) ||
                (drawerState.isOpen && isGameOpened(navController)),
    ) {
        MainContent(navController, customTopAppBarData)
    }
}

@Composable
private fun isGameOpened(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val gameVisible = (navBackStackEntry?.destination?.route ?: "") == ScreenRoute.GameScreen.route
    return gameVisible
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
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