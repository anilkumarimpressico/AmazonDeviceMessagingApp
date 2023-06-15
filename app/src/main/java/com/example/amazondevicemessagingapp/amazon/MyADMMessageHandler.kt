package com.example.amazondevicemessagingapp.amazon

import android.content.Context
import android.content.Intent
import android.util.Log
import com.amazon.device.messaging.ADMMessageHandlerJobBase
import com.example.amazondevicemessagingapp.R
import com.example.amazondevicemessagingapp.amazon.ADMHelper.MyTag

/*This class extends the ADMMessageHandlerJobBase class, which is provided by the ADM SDK.
It handles ADM-specific callbacks and actions related to registration, unregistration, and message reception.*/
class MyADMMessageHandler : ADMMessageHandlerJobBase() {

    private val TAG = "MyADMMessageHandler"
    override fun onMessage(context: Context?, intent: Intent?) {


        // Extract the message content from the extras attached to the com.amazon.device.messaging.intent.RECEIVE intent.

        // Create strings to access the message and timeStamp fields from the JSON data.
        val msgKey = context?.getString(R.string.json_data_msg_key)
        val timeKey = context?.getString(R.string.json_data_time_key)

        // Obtain the intent action that will be triggered in onMessage() callback.
        val intentAction = context?.getString(R.string.intent_msg_action)

        // Obtain the extras that were included in the intent.
        val extras = intent?.extras

        // Extract the message and time from the extras in the intent.
        // ADM makes no guarantees about delivery or the order of messages.
        // Due to varying network conditions, messages may be delivered more than once.
        // Your app must be able to handle instances of duplicate messages.
        val msg = extras?.getString(msgKey)
        val time = extras?.getString(timeKey)
        Log.d(MyTag, "onMessage:  $msg")
    }

    override fun onRegistrationError(p0: Context?, error: String?) {
        Log.d(MyTag, "onRegistrationError: $error ")
    }

    override fun onRegistered(context: Context?, newRegistrationId: String?) {
        // You start the registration process by calling startRegister() in your Main
        // Activity. When the registration ID is ready, ADM calls onRegistered() on
        // your app. Transmit the passed-in registration ID to your server, so your
        // server can send messages to this app instance. onRegistered() is also
        // called if your registration ID is rotated or changed for any reason; your
        // app should pass the new registration ID to your server if this occurs.
        // Your server needs to be able to handle a registration ID up to 1536 characters
        // in length.

        // The following is an example of sending the registration ID to your
        // server via a header key/value pair over HTTP.
        // You start the registration process by calling startRegister() in your Main
        // Activity. When the registration ID is ready, ADM calls onRegistered() on
        // your app. Transmit the passed-in registration ID to your server, so your
        // server can send messages to this app instance. onRegistered() is also
        // called if your registration ID is rotated or changed for any reason; your
        // app should pass the new registration ID to your server if this occurs.
        // Your server needs to be able to handle a registration ID up to 1536 characters
        // in length.

        // The following is an example of sending the registration ID to your
        // server via a header key/value pair over HTTP.

        Log.d(TAG, "onRegistered:$newRegistrationId ")
        /*val url = URL("")
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.doInput = true
        con.useCaches = false
        con.requestMethod = "POST"
        con.setRequestProperty("RegistrationId", newRegistrationId)
        con.responseCode*/
    }

    override fun onUnregistered(p0: Context?, p1: String?) {

    }
}