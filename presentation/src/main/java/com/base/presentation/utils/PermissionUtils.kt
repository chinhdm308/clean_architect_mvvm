package com.base.presentation.utils

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker


object PermissionUtils {

    /*Method to check the storage permission is granted or not... */
    fun storagePermission(activity: Activity): Boolean {
        if (PermissionChecker.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PermissionChecker.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                AppConstants.MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE
            )
            return false
        } else return true
    }
}