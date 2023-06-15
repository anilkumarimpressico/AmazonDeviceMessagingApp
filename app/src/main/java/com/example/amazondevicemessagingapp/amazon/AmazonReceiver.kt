package com.example.amazondevicemessagingapp.amazon

import com.amazon.device.messaging.ADMMessageReceiver

class AmazonReceiver : ADMMessageReceiver(SampleADMMessageHandler::class.java) {
        init {
            if(ADMHelper.IS_ADM_V2){
                registerJobServiceClass(MyADMMessageHandler::class.java, ADMHelper.JOB_ID)
            }
        }

}