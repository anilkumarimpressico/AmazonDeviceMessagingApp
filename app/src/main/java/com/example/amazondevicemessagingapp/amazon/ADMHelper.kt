package com.example.amazondevicemessagingapp.amazon

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.amazondevicemessagingapp.MainActivity

/**
 * The ADMHelper class is a helper class.
 *
 * @version Revision: 1, Date: 11/20/2019
 */
object ADMHelper {
    const val ADM_CLASSNAME = "com.amazon.device.messaging.ADM"
    const val ADMV2_HANDLER = "com.amazon.device.messaging.ADMMessageHandlerJobBase"
    const val JOB_ID = 1324124
    const val CHANNEL_ID = "notification_channel"
    const val CHANNEL_NAME = "Notification Channel"
    const val MyTag="MYTestTag"

    val IS_ADM_AVAILABLE: Boolean
    val IS_ADM_V2: Boolean

    private fun isClassAvailable(className: String): Boolean {
        return try {
            Class.forName(className)
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    init {
        IS_ADM_AVAILABLE = isClassAvailable(ADM_CLASSNAME)
        IS_ADM_V2 = if (IS_ADM_AVAILABLE) isClassAvailable(ADMV2_HANDLER) else false
    }

    /**
     * This method posts a notification to the notification manager.
     *
     * @param msgKey String to access the message field from data JSON.
     * @param timeKey String to access the timeStamp field from data JSON.
     * @param intentAction Intent action that will be triggered in onMessage() callback.
     * @param msg Message that is included in the ADM message.
     * @param time Timestamp of the ADM message.
     */
    fun createADMNotification(
        context: Context,
        msgKey: String,
        timeKey: String,
        intentAction: String,
        msg: String,
        time: String
    ) {
        /* Clicking the notification should bring up the MainActivity. */
        /* Intent FLAGS prevent opening multiple instances of MainActivity. */
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(msgKey, msg)
            putExtra(timeKey, time)
            action = intentAction + time
        }

        val pendingIntent =
            PendingIntent.getActivity(context, 0, notificationIntent, Intent.FILL_IN_CATEGORIES or Intent.FILL_IN_PACKAGE)

        val builder: Notification.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = Notification.Builder(context, CHANNEL_ID)
                .setContentTitle("ADM Message Received!")
                .setContentText(msg)

                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            builder = Notification.Builder(context)
                .setContentTitle("ADM Message Received!")
                .setContentText(msg)
                //.setSmallIcon(R.drawable.iv_notification_image)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        val notification: Notification = builder.build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /*notificationManager.notify(
            context.resources.getInteger(R.integer.sample_app_notification_id),
            notification
        )*/
    }
}
