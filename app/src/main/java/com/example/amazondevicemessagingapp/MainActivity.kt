package com.example.amazondevicemessagingapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.amazon.device.messaging.ADM
import com.amazon.device.messaging.development.ADMManifest
import com.example.amazondevicemessagingapp.amazon.ADMHelper.MyTag
import com.example.amazondevicemessagingapp.amazon.MyServerMsgHandler
import com.example.amazondevicemessagingapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        /* Register app with ADM. */
        //register();
        var available = false
        try {
            Class.forName("com.amazon.device.messaging.ADM")
            available = true
        } catch (e: ClassNotFoundException) {
            Log.e(MyTag, "onCreate: ${e.message}", )
            // Handle the exception.
        }
        if (available) {
            ADMManifest.checkManifestAuthoredProperly(this)
        }
        mBinding.btnRegister.setOnClickListener {
        registerADM()
        }

    }

    private fun registerADM() {
        try {
            val adm:ADM=ADM(applicationContext)
            if(adm.isSupported){
                if(adm.registrationId==null){
                    adm.startRegister()
                }
                else{
                    val myServiceMsgHandler:MyServerMsgHandler=MyServerMsgHandler()
                    //myServiceMsgHandler.registerAppInstance(applicationContext,adm.registrationId)
                }
            }
        }
        catch (e: Exception) {
            Log.e("MyTag", "registerADM: ", )
        }
    }
}