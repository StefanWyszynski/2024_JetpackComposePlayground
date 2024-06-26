package com.thwackstudio.permissions_util.domain.data

interface PermissionsDialogHelper {
    fun arePermissionsPermanentlyDenied(): Boolean
    fun getDescription(): String
    fun getPermissionInfo(): PermissionInfo
    fun launchPermissionRequest()
    fun onDismiss()
    fun onDeny()
    fun onConfirm()
    fun goToSystemSettings()
}