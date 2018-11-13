package com.pants.chance.race.util

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun checkPermission(permission: String, activity: Activity) {
    val hasntPermissions = ContextCompat.checkSelfPermission(
        activity, permission
    ) != PackageManager.PERMISSION_GRANTED

    if (hasntPermissions) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(permission), 123
        )
    }
}
