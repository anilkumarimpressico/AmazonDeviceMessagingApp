package com.example.amazondevicemessagingapp

import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.amazondevicemessagingapp.util.FileLoggingTree
import com.example.amazondevicemessagingapp.util.FileLoggingTreeUpdate
import timber.log.Timber
import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class AppLogs:Application() {
    private  val TAG = "AppLogs"
    private lateinit var contentResolver: ContentResolver

    override fun onCreate() {
        super.onCreate()
        contentResolver = applicationContext.contentResolver
        //MultiDex.install(applicationContext)
        Log.d("Application Class", "onCreate: ")
        /*try {
            if(hasWritePermission()) {
                val fileLoggingTree = FileLoggingTreeUpdate(contentResolver)
                if(BuildConfig.DEBUG)
                    Timber.plant(fileLoggingTree)
            }
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: ", )
        }*/
        //    setupTimber()
        //Timber.d("App Launches")
    }
    private fun hasWritePermission(): Boolean {
        val writePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        return ContextCompat.checkSelfPermission(this, writePermission) == PackageManager.PERMISSION_GRANTED
    }

}
