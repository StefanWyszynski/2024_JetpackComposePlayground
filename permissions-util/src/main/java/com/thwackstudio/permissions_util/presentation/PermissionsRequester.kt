package com.thwackstudio.permissions_util.presentation

import android.content.Context
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
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

    enum class PermissionProcessState {
        CALL_PERMISSION_LAUNCHER_ON_START,
        CHECK_FOR_STATE_CHANGE,
        CALL_PERMISSION_LAUNCHER_IN_RATIONALE_DIALOG,
        SHOW_CUSTOM_RATIONALE_DIALOG,
        GO_TO_PERMISSION_SYSTEM_SETTINGS
    }

    private var allPermissionsGranted by mutableStateOf(false)

    @Composable
    fun RequestMultiplePermissions(onShowPermissionDialog: @Composable (PermissionsDialogHelper) -> Unit) {
        if (isAllPermissionsGranted()) {
            return
        }
        val processState = remember { mutableStateOf(PermissionProcessState.CALL_PERMISSION_LAUNCHER_ON_START) }
        val showRationaleDialogsCount = remember { mutableStateOf(0) }
        val permissionPermamentlyDenied = remember { mutableStateOf(false) }
        val resultLauncher = rememberResultLauncher(
            permissionPermamentlyDenied, showRationaleDialogsCount, processState
        )

        val context = LocalContext.current
        val numPermissionsToCheck = _permissions.getNotDeniedAndNotGrantedPermissions(context).count()
        when (processState.value) {
            PermissionProcessState.CALL_PERMISSION_LAUNCHER_ON_START -> {
                if (numPermissionsToCheck > 0) {
                    PermissionHelperUtils.CallPermissionLauncherUsingDisposableEffect(
                        resultLauncher, getPermissionsToLauncher(context), processState
                    )
                }
            }

            PermissionProcessState.CHECK_FOR_STATE_CHANGE -> {
                if (numPermissionsToCheck > 0) {
                    if (showRationaleDialogsCount.value == 0 &&
                        !isAllPermissionsGranted() && !permissionPermamentlyDenied.value
                    ) {
                        processState.value = PermissionProcessState.CALL_PERMISSION_LAUNCHER_ON_START
                    } else {
                        if (showRationaleDialogsCount.value > 0) {
                            processState.value = PermissionProcessState.SHOW_CUSTOM_RATIONALE_DIALOG
                        }
                    }
                }
            }

            PermissionProcessState.SHOW_CUSTOM_RATIONALE_DIALOG -> {
                val permissions = _permissions.getNotDeniedAndNotGrantedPermissions(context)
                for (permissionInfo in permissions) {
                    val customPermissionDialog = rememberPermissionDialogHelper(
                        permissionPermamentlyDenied,
                        permissionInfo,
                        launchRequestLauncher = {
                            processState.value = PermissionProcessState.CALL_PERMISSION_LAUNCHER_IN_RATIONALE_DIALOG
                            resultLauncher.launch(getPermissionsToLauncher(context))
                        },
                        showRationaleDialogsCount,
                        processState
                    )
                    onShowPermissionDialog(customPermissionDialog)
                }
            }

            PermissionProcessState.GO_TO_PERMISSION_SYSTEM_SETTINGS -> {
                ShowSettingsDialog()
                processState.value = PermissionProcessState.CALL_PERMISSION_LAUNCHER_ON_START
            }

            PermissionProcessState.CALL_PERMISSION_LAUNCHER_IN_RATIONALE_DIALOG -> {
                // wait for state change
            }
        }
    }

    @Composable
    private fun rememberResultLauncher(
        permamentlyDenied: MutableState<Boolean>,
        showRationaleDialogsCount: MutableState<Int>,
        processState: MutableState<PermissionProcessState>
    ): ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>> {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val resultLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    scope.launch {
                        resetGrantedPermissionsInfoStatus(permissions)

                        val allPermissionsGranted = permissions.values.all { isGranted ->
                            isGranted
                        }
                        Log.e(
                            "TAG", "isLaunchedToFastMeansSystemIsPermamentyDeniedPermissions: " +
                                    isLaunchedToFastMeansSystemIsPermamentyDeniedPermissions()
                        )
                        permamentlyDenied.value = (!allPermissionsGranted) &&
                                isLaunchedToFastMeansSystemIsPermamentyDeniedPermissions()
                        if (!allPermissionsGranted || permamentlyDenied.value) {
                            showRationaleDialogsCount.value =
                                _permissions.getNotDeniedAndNotGrantedPermissions(
                                    context
                                ).count()
                        } else {
                            showRationaleDialogsCount.value = 0
                        }
                        processState.value = PermissionProcessState.CHECK_FOR_STATE_CHANGE
                    }
                })
        return resultLauncher
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
        launchRequestLauncher: () -> Unit,
        showPermissionDialogsCount: MutableState<Int>,
        processState: MutableState<PermissionProcessState>,
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
                    processState.value = PermissionProcessState.CHECK_FOR_STATE_CHANGE
                }

                override fun onDeny() {
                    permission.isDeniedByUserInRationaleDialog = true
                    showPermissionDialogsCount.value--
                    processState.value = PermissionProcessState.CHECK_FOR_STATE_CHANGE
                }

                override fun goToSystemSettings() {
                    processState.value = PermissionProcessState.GO_TO_PERMISSION_SYSTEM_SETTINGS
                }

                override fun onConfirm() {
                    onDismiss()
                    if (arePermissionsPermamentyDenied()) {
                        goToSystemSettings()
                    } else {
                        launchPermissionRequest()
                    }
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