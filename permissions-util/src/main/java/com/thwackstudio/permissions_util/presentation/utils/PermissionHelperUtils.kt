package com.thwackstudio.permissions_util.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.thwackstudio.permissions_util.presentation.PermissionsRequester
import com.thwackstudio.permissions_util.presentation.lastPermissionRequestLaunchedAt

fun isPermissionGranted(context: Context, permissionName: String) = permissionName.isNotEmpty() &&
        ContextCompat.checkSelfPermission(
            context,
            permissionName
        ) == PackageManager.PERMISSION_GRANTED


fun shouldShowRationale(activity: Activity, permissionName: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName)
}

internal object PermissionHelperUtils {

    @Composable
    internal fun <T, R> CallPermissionLauncherUsingDisposableEffect(
        resultLauncher: ManagedActivityResultLauncher<T, R>,
        permissions: T,
        permProcessState: MutableState<PermissionsRequester.PermissionProcessState>
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(key1 = lifecycleOwner, effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    lastPermissionRequestLaunchedAt = System.currentTimeMillis()
                    permProcessState.value = PermissionsRequester.PermissionProcessState.CALL_PERMISSION_LAUNCHER_IN_RATIONALE_DIALOG
                    resultLauncher.launch(permissions)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        })
    }

}