package com.jetpackcompose.playground.main.presentation.data

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
sealed class ScreenRoute(val route: String) {
    data object GameScreen : ScreenRoute("GameScreen")
    data object SearchUser : ScreenRoute("searchUser")
    data object SearchRepo : ScreenRoute("searchRepo")
    data object CameraXTest : ScreenRoute("CameraXTest")
    data object MapsTest : ScreenRoute("MapTest")
    data object RoomTask : ScreenRoute("RoomTaskTest/{nestedScreen}") {

        fun namedNavArguments() =
            listOf(navArgument("nestedScreen") { type = NavType.StringType })

        data object Main : ScreenRoute("Main") {

            fun getFullPath() = RoomTask.route.replace("{nestedScreen}", Main.route)

            override fun navigate(navController: NavController, nav: NavOptions) {
                navController.navigate(
                    RoomTask.route.replace("{nestedScreen}", Main.route),
                    navOptions = nav
                )
            }
        }

        data object NewTask : ScreenRoute("NewTask") {
            fun getFullPath() = RoomTask.route.replace("{nestedScreen}", NewTask.route)
            override fun navigate(navController: NavController, nav: NavOptions) {
                navController.navigate(
                    RoomTask.route.replace("{nestedScreen}", NewTask.route),
                    navOptions = nav
                )
            }
        }

        override fun navigate(navController: NavController, nav: NavOptions) {
            navController.navigate(
                RoomTask.route.replace("{nestedScreen}", Main.route),
                navOptions = nav
            )
        }
    }

    data object RealmTask : ScreenRoute("RealmTaskTest/{nestedScreen}") {

        fun namedNavArguments() =
            listOf(navArgument("nestedScreen") { type = NavType.StringType })

        data object Main : ScreenRoute("Main") {
            fun getFullPath() = RealmTask.route.replace("{nestedScreen}", Main.route)
            override fun navigate(navController: NavController, nav: NavOptions) {
                navController.navigate(
                    RealmTask.route.replace("{nestedScreen}", Main.route),
                    navOptions = nav
                )
            }
        }

        data object NewTask : ScreenRoute("NewTask") {
            fun getFullPath() = RealmTask.route.replace("{nestedScreen}", NewTask.route)
            override fun navigate(navController: NavController, nav: NavOptions) {
                navController.navigate(
                    RealmTask.route.replace("{nestedScreen}", NewTask.route),
                    navOptions = nav
                )
            }
        }

        override fun navigate(navController: NavController, nav: NavOptions) {
            navController.navigate(
                RealmTask.route.replace("{nestedScreen}", Main.route),
                navOptions = nav
            )
        }
    }

    data object CryptoUtilTest : ScreenRoute("CryptoUtilTest")


    open fun navigate(navController: NavController, nav: NavOptions) {
        navController.navigate(route, navOptions = nav)
    }
}