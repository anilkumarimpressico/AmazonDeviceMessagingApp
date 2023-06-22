package com.example.amazondevicemessagingapp.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object StoragePermissionUtil {

    private const val STORAGE_PERMISSION__WRITE_REQUEST_CODE=123
    private const val STORAGE_PERMISSION_READ_REQUEST_CODE=124

    fun hasStorageWritePermission(activity: Activity):Boolean{
        val permission=Manifest.permission.WRITE_EXTERNAL_STORAGE
        val result=ContextCompat.checkSelfPermission(activity,permission)
        return result==PackageManager.PERMISSION_GRANTED
    }

    fun hasStorageReadPermission(activity: Activity):Boolean{
        val permission=Manifest.permission.READ_EXTERNAL_STORAGE
        val result=ContextCompat.checkSelfPermission(activity,permission)
        return result==PackageManager.PERMISSION_GRANTED
    }
    fun requestWritePermission(activity:Activity){
        val permission=Manifest.permission.WRITE_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(activity, arrayOf(permission),
            STORAGE_PERMISSION__WRITE_REQUEST_CODE)
    }
    fun requestReadPermission(activity:Activity){
        val permission=Manifest.permission.WRITE_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(activity, arrayOf(permission),
            STORAGE_PERMISSION_READ_REQUEST_CODE)
    }
    fun handleStoragePermissionResult(requestCode: Int, grantResults: IntArray): Boolean {
        if (requestCode == STORAGE_PERMISSION_READ_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true // Permission granted
            }
        }
        if (requestCode == STORAGE_PERMISSION__WRITE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true // Permission granted
            }
        }
        return false // Permission denied
    }

}