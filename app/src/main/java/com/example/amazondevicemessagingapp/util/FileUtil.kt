package com.example.amazondevicemessagingapp.util

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class FileUtil(
    private val contentResolver: ContentResolver,
    private val activity: Activity
) {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    fun createTextFile(): Uri? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "logs_$timeStamp.txt"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/MyApp")
            }
        }
        return contentResolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), contentValues)
        /*return if (hasWritePermission()) {

        } else {
            requestWritePermission()
            null
        }*/
    }

    private fun hasWritePermission(): Boolean {
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        return ContextCompat.checkSelfPermission(activity, writePermission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestWritePermission() {
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(activity, arrayOf(writePermission), PERMISSION_REQUEST_CODE)
    }

    fun handleWritePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        return if (requestCode == PERMISSION_REQUEST_CODE && permissions.isNotEmpty()) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
    }

    fun writeTextToFile(uri: Uri, text: String) {
        if (hasWritePermission()) {
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            outputStream.use { output ->
                output?.bufferedWriter()?.use { writer ->
                    writer.write(text)
                }
            }
        } else {
            Timber.e("Write permission not granted.")
        }
    }
}
