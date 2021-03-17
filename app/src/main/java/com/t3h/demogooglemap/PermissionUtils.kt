package com.t3h.demogooglemap

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    fun checkPermission(context: Context, permissions: MutableList<String>): Boolean {
        for (permission in permissions) {
            val result = ContextCompat.checkSelfPermission(context, permission)
//            PERMISSION_GRANTED: da dong y
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun showDialogPermission(
        activity: Activity,
        permissions: MutableList<String>,
        requestCode: Int
    ) {
        val permissionArr = arrayOfNulls<String>(permissions.size)
        for (i in 0..permissions.size - 1) {
            permissionArr[i] = permissions[i]
        }
        ActivityCompat.requestPermissions(
            activity,
            permissionArr,
            requestCode
        )
    }

}