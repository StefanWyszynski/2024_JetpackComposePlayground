package com.jetpackcompose.playground.common.presentation.data

import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.jetpackcompose.playground.R

enum class NavigationDrawerItems(
    @StringRes val title: Int,
    val navigate: (navController: NavController, nav: NavOptions) -> Unit
) {
    Game3DItem(R.string.navigation_item_3d_game, { navController, nav ->
        ScreenRoute.GameScreen.navigate(navController, nav)
    }),
    SearchGithubUserItem(R.string.navigation_item_search_github_users, { navController, nav ->
        ScreenRoute.SearchUser.navigate(navController, nav)
    }),
    SearchGithubReposItem(R.string.navigation_item_search_github_repos, { navController, nav ->
        ScreenRoute.SearchRepo.navigate(navController, nav)
    }),
    CameraXItem(R.string.navigation_item_camera_x_test, { navController, nav ->
        ScreenRoute.CameraXTest.navigate(navController, nav)
    }),
    RoomTaskItem(R.string.navigation_item_room_task_management, { navController, nav ->
        ScreenRoute.RoomTask.navigate(navController, nav)
    }),
    RealmTaskItem(R.string.navigation_item_realm_task_management, { navController, nav ->
        ScreenRoute.RealmTask.navigate(navController, nav)
    }),
    CryptoUtilItem(R.string.navigation_item_crypto_util_test, { navController, nav ->
        ScreenRoute.CryptoUtilTest.navigate(navController, nav)
    })
}