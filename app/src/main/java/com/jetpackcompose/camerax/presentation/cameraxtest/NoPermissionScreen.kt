package com.jetpackcompose.camerax.presentation.cameraxtest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoPermissionScreen(permission: PermissionState, requestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textToShow = if (permission.shouldShowRationale) {
            "The camera is important for this app. Please grant the permission."
        } else {
            "Camera permission required for this feature to be available. " +
                    "Please grant the permission"
        }
        Text(textToShow)
        Button(onClick = requestPermission) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "Camera")
            Text(text = "Grant permission")
        }
    }
}