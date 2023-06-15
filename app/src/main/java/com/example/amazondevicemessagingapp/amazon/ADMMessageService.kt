package com.example.amazondevicemessagingapp.amazon

import android.app.Service
import android.content.Intent
import android.os.IBinder


class ADMMessageService : Service() {
    private lateinit var messageHandler: MyADMMessageHandler
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        messageHandler= MyADMMessageHandler()
    }

}