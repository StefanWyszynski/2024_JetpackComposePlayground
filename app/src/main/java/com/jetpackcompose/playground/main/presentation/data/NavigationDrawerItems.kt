package com.jetpackcompose.playground.main.presentation.data

import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.jetpackcompose.playground.R

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
enum class NavigationDrawerItems(
    @StringRes val title: Int,
    val navigate: (navController: NavController, nav: NavOptions.Builder) -> Unit
) {
    Game3DItem(R.string.navigation_item_3d_game, { navController, nav ->
        ScreenRoute.GameScreen.navigate(navController, nav)
    }),
    SearchGithubUserItem(R.string.navigation_item_search_github_users, { navController, nav ->
        navController.popBackStack(ScreenRoute.SearchUser, inclusive = true)
        navController.navigate(ScreenRoute.SearchUser)
    }),
    SearchGithubReposItem(R.string.navigation_item_search_github_repos, { navController, nav ->
        navController.popBackStack(ScreenRoute.SearchRepo, inclusive = true)
        navController.navigate(ScreenRoute.SearchRepo)
    }),
    CameraXItem(R.string.navigation_item_camera_x_test, { navController, nav ->
        navController.popBackStack(ScreenRoute.CameraXTest, inclusive = true)
        navController.navigate(ScreenRoute.CameraXTest)
    }),
    RoomTaskItem(R.string.navigation_item_room_task_management, { navController, nav ->
        navController.popBackStack(ScreenRoute.RoomTask(0), inclusive = true)
        navController.navigate(ScreenRoute.RoomTask(0))
    }),
    RealmTaskItem(R.string.navigation_item_realm_task_management, { navController, nav ->
        navController.popBackStack(ScreenRoute.RealmTask(0), inclusive = true)
        navController.navigate(ScreenRoute.RealmTask(0))
    }),
    CryptoUtilItem(R.string.navigation_item_crypto_util_test, { navController, nav ->
        navController.popBackStack(ScreenRoute.CryptoUtilTest, inclusive = true)
        navController.navigate(ScreenRoute.CryptoUtilTest)
    })
}