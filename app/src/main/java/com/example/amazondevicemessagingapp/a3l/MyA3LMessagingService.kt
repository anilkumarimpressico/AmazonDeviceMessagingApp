package com.example.amazondevicemessagingapp.a3l

import android.content.Context
import android.util.Log
import com.amazon.A3L.messaging.A3LMessagingService
import com.amazon.A3L.messaging.RemoteMessage

class MyA3LMessagingService : A3LMessagingService(){


    override fun onMessageReceived(context: Context?, remoteMessage: RemoteMessage?) {
        Log.d("MyTag", "onMessageReceived: ${remoteMessage?.data.toString()}")
    }

    override fun onNewToken(context: Context?, token: String?) {
        Log.d("MyTag", " A3L Messaging onNewToken: $token")
    }

}
