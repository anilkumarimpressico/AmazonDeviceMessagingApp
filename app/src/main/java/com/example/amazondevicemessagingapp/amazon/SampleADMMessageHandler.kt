package com.example.amazondevicemessagingapp.amazon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.amazon.device.messaging.ADMConstants
import com.amazon.device.messaging.ADMMessageHandlerBase
import com.example.amazondevicemessagingapp.R

import java.util.HashMap

class SampleADMMessageHandler :ADMMessageHandlerBase {
    /** Tag for logs. */
    private val TAG = "ADMSampleIntentBase"

    /**
     * Class constructor.
     */
    constructor() : super(SampleADMMessageHandler::class.java.name)

    /**
     * Class constructor, including the className argument.
     *
     * @param className The name of the class.
     */
    constructor(className: String) : super(className)

    override fun onMessage(intent: Intent) {
        Log.i(TAG, "SampleADMMessageHandler:onMessage")

        /* String to access message field from data JSON. */
        val msgKey = getString(R.string.json_data_msg_key)

        /* String to access timeStamp field from data JSON. */
        val timeKey = getString(R.string.json_data_time_key)

        /* Intent action that will be triggered in onMessage() callback. */
        val intentAction = getString(R.string.intent_msg_action)

        /* Extras that were included in the intent. */
        val extras: Bundle? = intent.extras

        verifyMD5Checksum(extras)

        /* Extract message from the extras in the intent. */
        val msg: String? = extras?.getString(msgKey)
        val time: String? = extras?.getString(timeKey)

        if (msg == null || time == null) {
            Log.w(
                TAG, "SampleADMMessageHandler:onMessage Unable to extract message data." +
                        "Make sure that msgKey and timeKey values match data elements of your JSON message"
            )
        }

        /* Create a notification with message data. */
        /* This is required to test cases where the app or device may be off. */
        ADMHelper.createADMNotification(this, msgKey, timeKey, intentAction, msg!!, time!!)

        /* Intent category that will be triggered in onMessage() callback. */
        val msgCategory = getString(R.string.intent_msg_category)

        /* Broadcast an intent to update the app UI with the message. */
        /* The broadcast receiver will only catch this intent if the app is within the onResume state of its lifecycle. */
        /* User will see a notification otherwise. */
        val broadcastIntent = Intent().apply {
            action = intentAction
            addCategory(msgCategory)
            putExtra(msgKey, msg)
            putExtra(timeKey, time)
        }
        this.sendBroadcast(broadcastIntent)
    }


    override fun onRegistrationError(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onRegistered(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onUnregistered(p0: String?) {
        TODO("Not yet implemented")
    }

    /**
     * This method verifies the MD5 checksum of the ADM message.
     *
     * @param extras Extra that was included with the intent.
     */
    private fun verifyMD5Checksum(extras: Bundle?) {
        /* String to access consolidation key field from data JSON. */
        val consolidationKey = getString(R.string.json_data_consolidation_key)

        val extrasKeySet = extras?.keySet()
        val extrasHashMap = HashMap<String, String>()
        extrasKeySet?.let {
            for (key in it) {
                if (key != ADMConstants.EXTRA_MD5 && key != consolidationKey) {
                    extrasHashMap[key] = extras.getString(key).toString()
                }
            }
        }
        val md5 = ADMSampleMD5ChecksumCalculator.calculateChecksum(extrasHashMap)
    }
}