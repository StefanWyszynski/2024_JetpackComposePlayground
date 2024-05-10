package com.jetpackcompose.playground.main.presentation.data

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

object TaskSubScreen {
    val TaskMain = 0
    val TaskNew = 1
}

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@Serializable
sealed class ScreenRoute() {
    @Serializable
    @Parcelize
    object GameScreen : ScreenRoute(), Parcelable

    @Serializable
    @Parcelize
    object SearchUser : ScreenRoute(), Parcelable

    @Serializable
    @Parcelize
    object SearchRepo : ScreenRoute(), Parcelable

    @Serializable
    @Parcelize
    object CameraXTest : ScreenRoute(), Parcelable

    @Serializable
    @Parcelize
    object MapsTest : ScreenRoute(), Parcelable

    @Serializable
    @Parcelize
    data class RoomTask(val nastedScreen: Int) : ScreenRoute(), Parcelable

    @Serializable
    @Parcelize
    data class RealmTask(val nastedScreen: Int) : ScreenRoute(), Parcelable

    @Serializable
    @Parcelize
    object CryptoUtilTest : ScreenRoute(), Parcelable


    open fun navigate(navController: NavController, nav: NavOptions.Builder) {
        navController.navigate(this, navOptions = nav.build())
    }
}