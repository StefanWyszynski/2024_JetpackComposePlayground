package com.jetpackcompose.playground.camerax.presentation

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jetpackcompose.playground.camerax.presentation.viewmodel.CameraXViewModel
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData
import com.thwackstudio.permissions_util.domain.data.PermissionInfo
import com.thwackstudio.permissions_util.domain.data.PermissionsContainer
import com.thwackstudio.permissions_util.domain.data.PermissionsDialogHelper
import com.thwackstudio.permissions_util.presentation.rememberPermissionsRequester

@Composable
fun CameraXScreenContainer(
    customTopAppBarData: CustomTopAppBarData,
    hiltViewModel: CameraXViewModel
) {
    val permissionsGrantBuilder = rememberPermissionsBuilder()
    val permissionsRequester = rememberPermissionsRequester(permissionsGrantBuilder)

    if (permissionsRequester.isAllPermissionsGranted()) {
        CameraXScreenContent(customTopAppBarData, hiltViewModel)
    } else {
        NoPermissionScreen {}

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
}