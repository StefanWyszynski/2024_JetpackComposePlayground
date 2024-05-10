package com.jetpackcompose.playground.maps.presentation

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.thwackstudio.permissions_util.domain.data.PermissionInfo
import com.thwackstudio.permissions_util.domain.data.PermissionsContainer
import com.thwackstudio.permissions_util.domain.data.PermissionsDialogHelper
import com.thwackstudio.permissions_util.presentation.DefaultPermissionDialog
import com.thwackstudio.permissions_util.presentation.rememberPermissionsRequester

@Composable
fun GoogleMapScreen() {
    val permissionsGrantBuilder = rememberPermissionsBuilder()
    val permissionsRequester = rememberPermissionsRequester(permissionsGrantBuilder)

    if (permissionsRequester.isAllPermissionsGranted()) {
        GoogleMapTest()
    } else {
        MapNoPermissionScreen {}

        permissionsRequester.RequestMultiplePermissions(onShowPermissionDialog = { permissionDialogHelper: PermissionsDialogHelper ->
            DefaultPermissionDialog(permissionsDialogHelper = permissionDialogHelper)
//            PermissionDialog(permissionDialogHelper)
        })
    }
}

@Composable
private fun rememberPermissionsBuilder() = remember {
    PermissionsContainer()
        .addPermission(
            PermissionInfo(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                "Need coarse location",
                "Change permission in settings"
            )
        )
        .addPermission(
            PermissionInfo(
                Manifest.permission.ACCESS_FINE_LOCATION,
                "Need fine location",
                "Change permission in settings"
            )
        )
}

@Composable
fun GoogleMapTest() {
    Box(modifier = Modifier.fillMaxSize()) {
        var uiSettings by remember { mutableStateOf(MapUiSettings()) }
        val properties by remember { mutableStateOf(MapProperties(mapType = MapType.SATELLITE)) }
        val singapore = LatLng(1.35, 103.87)
        val cameraState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraState,
            properties = properties,
            uiSettings = uiSettings
        ) {
            Switch(
                checked = uiSettings.zoomControlsEnabled,
                onCheckedChange = {
                    uiSettings = uiSettings.copy(zoomControlsEnabled = it)
                }
            )
//            Marker(
//                state = MarkerState(position = singapore),
//                title = "Singapore",
//                snippet = "Marker in Singapore"
//            )
        }
    }
}

