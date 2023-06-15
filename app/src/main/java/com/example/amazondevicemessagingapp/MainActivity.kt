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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        /* Register app with ADM. */
        //register();

        try {
            Class.forName("com.amazon.device.messaging.ADM")
           /* val receiverPermission = ADMManifest.getReceiverPermission(this)
            Log.d("receiverPermission", "onCreate:  $receiverPermission")*/
            //ADMManifest.checkManifestAuthoredProperly(this)
        } catch (e: ClassNotFoundException) {
            Log.e(MyTag, "onCreate: ${e.message}", )
            // Handle the exception.
        }
        mBinding.btnRegister.setOnClickListener {
        registerADM()
        }

    }

    private fun registerADM() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val adm: ADM = ADM(applicationContext)
                if (adm.isSupported) {
                    if (adm.registrationId == null) {
                        adm.startRegister()
                    } else {
                        val myServiceMsgHandler: MyServerMsgHandler = MyServerMsgHandler()
                        myServiceMsgHandler.registerAppInstance(
                            applicationContext,
                            adm.registrationId
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("MyTag", "registerADM: ${e.message}",)
            }
        }
    }
}