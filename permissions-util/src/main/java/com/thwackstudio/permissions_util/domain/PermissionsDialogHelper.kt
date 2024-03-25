package com.thwackstudio.permissions_util.domain

interface PermissionsDialogHelper {
    fun arePermissionsPermamentyDenied(): Boolean
    fun getDescription(): String
    fun getPermissionInfo(): PermissionInfo
    fun launchPermissionRequest()
    fun onDismiss()
    fun onDeny()
    fun onConfirm()
    fun goToSystemSettings()
}