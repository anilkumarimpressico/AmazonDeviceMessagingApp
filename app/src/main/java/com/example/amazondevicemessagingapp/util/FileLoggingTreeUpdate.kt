package com.example.amazondevicemessagingapp.util

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import timber.log.Timber
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileLoggingTreeUpdate(private val contentResolver: ContentResolver) : Timber.Tree() {

    private var logFileUri: Uri? = null
    private var outputStream: OutputStream? = null

    init {
        logFileUri = getLogFileUri()
        logFileUri?.let { uri ->
            outputStream = contentResolver.openOutputStream(uri)
        }
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
            val logMessage = "$timestamp $tag/$priority: $message\n"

            outputStream?.let { stream ->
                stream.write(logMessage.toByteArray())
            }
        } catch (e: IOException) {
            Log.e("FileLoggingTree", "Failed to write log to file: ${e.message}")
        }
    }

    private fun createLogFile(): Uri? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val logFileName = "app_logs.txt"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, logFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/MyAppLogs")
            }
        }

        return contentResolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), contentValues)
    }
    private fun getLogFileUri(): Uri? {
        val logFileName = "app_logs.txt"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, logFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/MyAppLogs")
            }
        }

        val queryUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(logFileName)
        val cursor = contentResolver.query(queryUri, null, selection, selectionArgs, null)

        val fileUri: Uri?
        if (cursor != null && cursor.moveToFirst()) {
            val fileIdIndex = cursor.getColumnIndex(MediaStore.MediaColumns._ID)
            val fileId = cursor.getLong(fileIdIndex)
            fileUri = Uri.withAppendedPath(queryUri, fileId.toString())
            cursor.close()
        } else {
            fileUri = contentResolver.insert(queryUri, contentValues)
            cursor?.close()
        }

        return fileUri
    }
}
