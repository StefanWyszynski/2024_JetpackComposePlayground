package com.thwackstudio.permissions_util.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings

internal object ExtensionUtils {
    fun openApplicationSettings(context: Context) {
        val activity = context.findActivityEnsureExists()
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.parse("package:${activity.packageName}")
        }.also {
            activity.startActivity(it)
        }
    }

    fun Context.findActivityEnsureExists(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("Permissions should be called in the context of an Activity")
    }
}