package com.example.amazondevicemessagingapp.util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import timber.log.Timber
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ScopeFileUtil(context: Context) {

    companion object {
        private const val FILE_NAME = "my_log_file.txt"

        fun createTextFile(context: Context, text: String) {
            try {
                val contentResolver = context.contentResolver
                val fileUri = createFileUri(context)
                val outputStream = contentResolver.openOutputStream(fileUri,"wa")

                outputStream?.use { writer ->
                    BufferedWriter(OutputStreamWriter(writer)).use { bufferedWriter ->
                        bufferedWriter.write(text)
                    }
                }
                // File created successfully
            } catch (e: Exception) {
                Log.e("FileUtil", "Failed to create text file: ${e.message}")
            }
        }

        fun appendTextToFile(context: Context, text: String) {
            try {
                val contentResolver = context.contentResolver
                val fileUri = createFileUri(context)
                val outputStream = contentResolver.openOutputStream(fileUri, "wa")

                outputStream?.use { writer ->
                    BufferedWriter(OutputStreamWriter(writer)).use { bufferedWriter ->
                        bufferedWriter.append(text)
                    }
                }
                // Text appended to file successfully
            } catch (e: Exception) {
                Log.e("FileUtil", "Failed to append text to file: ${e.message}")
            }
        }

        fun readTextFile(context: Context): String? {
            try {
                val contentResolver = context.contentResolver
                val fileUri = createFileUri(context)
                val inputStream = contentResolver.openInputStream(fileUri)

                inputStream?.use { reader ->
                    BufferedReader(InputStreamReader(reader)).use { bufferedReader ->
                        val stringBuilder = StringBuilder()
                        var line: String?
                        while (bufferedReader.readLine().also { line = it } != null) {
                            stringBuilder.append(line).append("\n")
                        }
                        return stringBuilder.toString()
                    }
                }
            } catch (e: Exception) {
                Log.e("FileUtil", "Failed to read text file: ${e.message}")
                return null
            }
            return ""

        }
        private fun createFileUri(context: Context): Uri {
            val fileDir = context.filesDir
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "my_logs_$timeStamp.txt"
            val file = File(fileDir, fileName)
            return file.toUri()
        }

    }
}
