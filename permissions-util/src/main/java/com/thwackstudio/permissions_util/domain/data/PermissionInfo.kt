package com.thwackstudio.permissions_util.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PermissionInfo(
    var name: String,
    var description: String? = null,
    var rationaleDescription: String? = null,
    var isDeniedByUserInRationaleDialog: Boolean = false
) : Parcelable