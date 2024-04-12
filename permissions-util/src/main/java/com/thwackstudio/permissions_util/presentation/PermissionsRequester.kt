package com.thwackstudio.permissions_util.presentation

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.thwackstudio.permissions_util.domain.data.PermissionInfo
import com.thwackstudio.permissions_util.domain.data.PermissionsContainer
import com.thwackstudio.permissions_util.domain.data.PermissionsDialogHelper
import com.thwackstudio.permissions_util.presentation.utils.ExtensionUtils
import com.thwackstudio.permissions_util.presentation.utils.PermissionHelperUtils
import com.thwackstudio.permissions_util.presentation.utils.isPermissionGranted
import kotlinx.coroutines.launch

internal const val PERMISSIONS_CLICK_DELAY_MS = 200
internal var lastPermissionRequestLaunchedAt = 0L

@Composable
fun <T : PermissionsContainer<T>> rememberPermissionsRequester(permBuilder: PermissionsContainer<T>): PermissionsRequester {
    return remember(permBuilder._permissions) {
        val permissionsRequester = PermissionsRequester(permBuilder._permissions)
        permissionsRequester
    }
}

class PermissionsRequester(private var _permissions: List<PermissionInfo>) {

    private var allPermissionsGranted by mutableStateOf(false)

    @Composable
    fun RequestMultiplePermissions(onShowPermissionDialog: @Composable (PermissionsDialogHelper) -> Unit) {
        val context = LocalContext.current
        val notGrantedPermissionsCount =
            _permissions.getNotDeniedAndNotGrantedPermissions(context).count()
        val launcherAlreadyCalled = remember { mutableStateOf(false) }
        val showRationaleDialogsCount = remember { mutableStateOf(0) }
        if (isAllPermissionsGranted()) {
            return
        }
        val scope = rememberCoroutineScope()
        val showSystemSettings = remember { mutableStateOf(false) }
        val permissionPermamentlyDenied = remember { mutableStateOf(false) }

        if (notGrantedPermissionsCount > 0) {
            val callLauncherOnPermissionDialogCallback: (() -> Unit)
//
            val resultLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { permissions ->
                        scope.launch {
                            launcherAlreadyCalled.value = false
                            resetGrantedPermissionsInfoStatus(permissions)
                            allPermissionsGranted = permissions.values.all { isGranted ->
                                isGranted
                            }
                            permissionPermamentlyDenied.value = (!allPermissionsGranted) &&
                                    isLaunchedToFastMeansSystemIsPermamentyDeniedPermissions()
                            if (!allPermissionsGranted || permissionPermamentlyDenied.value) {
                                showRationaleDialogsCount.value =
                                    _permissions.getNotDeniedAndNotGrantedPermissions(
                                        context
                                    ).count()
                            } else {
                                showRationaleDialogsCount.value = 0
                            }

                        }
                    })


            if (!launcherAlreadyCalled.value &&
                showRationaleDialogsCount.value == 0 &&
                !allPermissionsGranted && !permissionPermamentlyDenied.value
            ) {
                PermissionHelperUtils.CallPermissionLauncherUsingDisposableEffect(
                    resultLauncher,
                    getPermissionsToLauncher(context),
                    launcherAlreadyCalled
                )
            } else {
                if (showRationaleDialogsCount.value > 0) {
                    callLauncherOnPermissionDialogCallback = {
                        launcherAlreadyCalled.value = true
                        resultLauncher.launch(getPermissionsToLauncher(context))
                    }
                    val permissions = _permissions.getNotDeniedAndNotGrantedPermissions(context)
                    for (permission in permissions) {
                        val customPermissionDialog = rememberPermissionDialogHelper(
                            permissionPermamentlyDenied,
                            permission, showSystemSettings,
                            launchRequestLauncher = {
                                callLauncherOnPermissionDialogCallback.invoke()
                            },
                            showRationaleDialogsCount
                        )
                        onShowPermissionDialog(customPermissionDialog)
                    }
                }
            }
            if (showSystemSettings.value) {
                ShowSettingsDialog()
                showSystemSettings.value = false
            }
        }
    }

    private fun resetGrantedPermissionsInfoStatus(permissions: Map<String, @JvmSuppressWildcards Boolean>) {
        for (key in permissions.keys) {
            val granted = permissions[key]
            granted?.let {
                if (granted) {
                    _permissions.find { it.name == key }?.isDeniedByUserInRationaleDialog =
                        false
                }
            }
        }
    }

    private fun getPermissionsToLauncher(context: Context) =
        _permissions.getNotDeniedAndNotGrantedPermissions(context)
            .map { it.name }.toList().toTypedArray()

    private fun isLaunchedToFastMeansSystemIsPermamentyDeniedPermissions() =
        (System.currentTimeMillis() - PERMISSIONS_CLICK_DELAY_MS < lastPermissionRequestLaunchedAt)

    fun List<PermissionInfo>.getNotDeniedAndNotGrantedPermissions(context: Context): List<PermissionInfo> {
        return this.filter {
            !it.isDeniedByUserInRationaleDialog && !isPermissionGranted(
                context,
                it.name
            )
        }
    }

    @Composable
    private fun ShowSettingsDialog() {
        ExtensionUtils.openApplicationSettings(LocalContext.current)
    }

    @Composable
    internal fun rememberPermissionDialogHelper(
        permissionPermamentlyDenied: MutableState<Boolean>,
        permission: PermissionInfo,
        showSystemSettings: MutableState<Boolean>,
        launchRequestLauncher: () -> Unit,
        showPermissionDialogsCount: MutableState<Int>,
    ): PermissionsDialogHelper {
        return remember {
            object : PermissionsDialogHelper {
                override fun arePermissionsPermamentyDenied(): Boolean {
                    return permissionPermamentlyDenied.value
                }

                override fun getDescription(): String {
                    return if (arePermissionsPermamentyDenied()) {
                        getPermissionInfo().rationaleDescription ?: ""
                    } else {
                        getPermissionInfo().description ?: ""
                    }
                }

                override fun getPermissionInfo(): PermissionInfo {
                    return permission
                }

                override fun launchPermissionRequest() {
                    showPermissionDialogsCount.value = 0
                    lastPermissionRequestLaunchedAt = System.currentTimeMillis()
                    launchRequestLauncher()
                }

                override fun onDismiss() {
                    showPermissionDialogsCount.value = 0
                }

                override fun onDeny() {
                    permission.isDeniedByUserInRationaleDialog = true
                    showPermissionDialogsCount.value--
                }

                override fun goToSystemSettings() {
                    showSystemSettings.value = true
                }

                override fun onConfirm() {
                    if (arePermissionsPermamentyDenied()) {
                        goToSystemSettings()
                    } else {
                        launchPermissionRequest()
                    }
                    onDismiss()
                }
            }
        }
    }

    @Composable
    fun isAllPermissionsGranted(): Boolean {
        val context = LocalContext.current
        if (_permissions.isEmpty()) {
            return true
        }
        allPermissionsGranted = _permissions.all { isPermissionGranted(context, it.name) }
        return allPermissionsGranted
    }
}