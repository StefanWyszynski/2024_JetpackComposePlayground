package com.jetpackcompose.playground.camerax.presentation.cameraxtest

import android.Manifest
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.thwackstudio.permissions_util.domain.PermissionInfo
import com.thwackstudio.permissions_util.domain.PermissionsDialogHelper
import com.thwackstudio.permissions_util.domain.PermissionsContainer
import com.thwackstudio.permissions_util.presentation.rememberPermissionsRequester
import kotlinx.coroutines.CoroutineScope

@Composable
fun CameraXScreenContainer(scope: CoroutineScope, drawerState: DrawerState) {
    val permissionsGrantBuilder = rememberPermissionsBuilder()
    val permissionsRequester = rememberPermissionsRequester(permissionsGrantBuilder)

    if (permissionsRequester.isAllPermissionsGranted()) {
        CameraXScreenContent(scope, drawerState)
    } else {
        NoPermissionScreen() {
        }

        permissionsRequester.RequestMultiplePermissions(onShowPermissionDialog = { permissionDialogHelper: PermissionsDialogHelper ->
//        DefaultPermissionDialog(permissionsDialogHelper = permissionDialogHelper)

            PermissionDialog(permissionDialogHelper)
        })
    }
}

@Composable
private fun rememberPermissionsBuilder() = remember {
    PermissionsContainer()
        .addPermission(
            PermissionInfo(
                Manifest.permission.CAMERA,
                "Need camera permission",
                "Change settings camera in perm dialog"
            )
        )
        .addPermission(
            PermissionInfo(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                "Need coarse location permission",
                "Change settings coarse location in perm dialog"
            )
        )
}