package com.thwackstudio.permissions_util.domain

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Parcelable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.parcelize.Parcelize

@Parcelize
data class PermissionInfo(
    var permissionName: String,
    var description: String? = null,
    var rationaleDescription: String? = null,
    var permissionDeniedByUser: Boolean = false
) : Parcelable {


    fun isPermissionGranted(context: Context) = permissionName.isNotEmpty() &&
            ContextCompat.checkSelfPermission(
                context,
                permissionName
            ) == PackageManager.PERMISSION_GRANTED


    fun shouldShowRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName)
    }
}
