package com.jetpackcompose.playground.common.presentation.data

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument

/*
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
sealed class ScreenRoute(val route: String) {
    object GameScreen : ScreenRoute("GameScreen")
    object SearchUser : ScreenRoute("searchUser")
    object SearchRepo : ScreenRoute("searchRepo")
    object CameraXTest : ScreenRoute("CameraXTest")
    object MapsTest : ScreenRoute("MapTest")
    object RoomTask : ScreenRoute("RoomTaskTest/{nestedScreen}") {

        fun namedNavArguments() =
            listOf(navArgument("nestedScreen") { type = NavType.StringType })

        object Main : ScreenRoute("Main") {

            fun getFullPath() = RoomTask.route.replace("{nestedScreen}", Main.route)

            override fun navigate(navController: NavController, nav: NavOptions) {
                navController.navigate(
                    RoomTask.route.replace("{nestedScreen}", Main.route),
                    navOptions = nav
                )
            }
        }

        object NewTask : ScreenRoute("NewTask") {
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

    object RealmTask : ScreenRoute("RealmTaskTest/{nestedScreen}") {

        fun namedNavArguments() =
            listOf(navArgument("nestedScreen") { type = NavType.StringType })

        object Main : ScreenRoute("Main") {
            fun getFullPath() = RealmTask.route.replace("{nestedScreen}", Main.route)
            override fun navigate(navController: NavController, nav: NavOptions) {
                navController.navigate(
                    RealmTask.route.replace("{nestedScreen}", Main.route),
                    navOptions = nav
                )
            }
        }

        object NewTask : ScreenRoute("NewTask") {
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

    object CryptoUtilTest : ScreenRoute("CryptoUtilTest")


    open fun navigate(navController: NavController, nav: NavOptions) {
        navController.navigate(route, navOptions = nav)
    }
}