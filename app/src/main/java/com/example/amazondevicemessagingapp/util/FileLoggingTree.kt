package com.example.amazondevicemessagingapp.util

import android.content.Context
import android.os.Environment
import android.util.Log
import timber.log.Timber
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class FileLoggingTree(private val context:Context) : Timber.Tree() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    /*private val logFile: File


    init {
        val logsDir = File(Environment.getExternalStorageDirectory(), "MyAppLogs")
        logsDir.mkdirs()
        logFile = File(logsDir, "app_logs.txt")
        logFile.createNewFile()
    }*/

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        try {
            Log.d("FileLoggingTree", "log: $message ")
            val log = buildLogEntry(priority, tag, message)
            //val logText = "${getPriorityString(priority)}/$tag: $message"
            ScopeFileUtil.createTextFile(context,log)

            /*FileOutputStream(logFile, true).use { stream ->
                stream.write(logEntry.toByteArray())
            }*/
        } catch (e: IOException) {
            Log.e("FileLoggingTree", "Error writing log to file", e)
        }
    }

    private fun buildLogEntry(priority: Int, tag: String?, message: String): String {
        val timestamp = dateFormat.format(Date())
        val priorityString = getPriorityString(priority)
        val logTag = tag ?: "NoTag"
        return "$timestamp $priorityString/$logTag: $message\n"
    }

    private fun getPriorityString(priority: Int): String {
        return when (priority) {
            Log.VERBOSE -> "VERBOSE"
            Log.DEBUG -> "DEBUG"
            Log.INFO -> "INFO"
            Log.WARN -> "WARN"
            Log.ERROR -> "ERROR"
            else -> "UNKNOWN"
        }
    }
}
