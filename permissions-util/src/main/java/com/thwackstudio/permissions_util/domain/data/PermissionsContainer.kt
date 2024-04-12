package com.thwackstudio.permissions_util.domain.data

internal interface PermissionsGrantBuilderBase<out T> {

}

open class PermissionsContainer<T : PermissionsContainer<T>> :
    PermissionsGrantBuilderBase<T> {

    internal var _permissions = arrayListOf<PermissionInfo>()

    fun addPermission(permissionName: PermissionInfo): PermissionsContainer<T> {
        _permissions.add(permissionName)
        return this
    }

}
