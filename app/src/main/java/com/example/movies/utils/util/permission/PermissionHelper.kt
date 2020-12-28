package com.example.movies.utils.util.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment


object PermissionHelper {

    fun hasPermission(context: Context, permission: String): Boolean {
        return ActivityCompat
            .checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(activity: Activity, permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions, CODE_PERMISSION)
        }
    }

    const val CODE_PERMISSION = 17

}