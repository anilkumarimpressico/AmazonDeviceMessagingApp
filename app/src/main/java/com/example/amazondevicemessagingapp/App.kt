package com.example.amazondevicemessagingapp

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.FirebaseApp

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.d("MyTag", "onCreate: ")
        FirebaseApp.initializeApp(applicationContext)
        val filename = "logs"+System.currentTimeMillis()+".txt"
        val content = "Log message to be written"

        val resolver: ContentResolver = applicationContext.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/MyLogs")
        }

        val uri = resolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), contentValues)

        uri?.let { fileUri ->
            resolver.openOutputStream(fileUri)?.use { outputStream ->
                outputStream.write(content.toByteArray())
                outputStream.close()
                Log.d("LOG", "Log written to file: $fileUri")
            }
        }
    }
}