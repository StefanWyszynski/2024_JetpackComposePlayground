package com.jetpackcompose.playground.common.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {
    @SuppressLint("SimpleDateFormat")
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(Date(millis))
    }
}