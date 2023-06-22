package com.example.amazondevicemessagingapp

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amazon.A3L.messaging.A3LMessaging
import com.amazon.A3L.messaging.exception.RegistrationException
import com.amazon.A3L.messaging.registration.InitCallbackResponse
import com.amazon.A3L.messaging.registration.OnInitCallback


import com.example.amazondevicemessagingapp.databinding.ActivityMainBinding
import com.example.amazondevicemessagingapp.util.FileLoggingTree
import com.example.amazondevicemessagingapp.util.FileLoggingTreeUpdate
import com.example.amazondevicemessagingapp.util.FileUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber
import java.sql.Time


class MainActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityMainBinding
    companion object {
        private const val REQUEST_WRITE_PERMISSION = 1
    }
    private lateinit var fileUtil: FileUtil
    private var textFileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        fileUtil = FileUtil(contentResolver, this)

        if (hasWritePermission()) {
//            val fileLoggingTree = FileLoggingTreeUpdate(contentResolver)
//            Timber.plant(fileLoggingTree)
            val fileLoggingTree=FileLoggingTree(this)
            Timber.plant(fileLoggingTree)
        } else {
            requestWritePermission()
        }
        getOSName()
        mBinding.btnFirebaseToken.setOnClickListener {
        //registerADM()

            generateToken()
        }

        mBinding.btnAmazonToken.setOnClickListener {
            generateAmazonToken()
        }
    }
    private fun getOSName():String {
        try {
            Timber.d(Build.MANUFACTURER + " " + Build.MODEL + " " + Build.DEVICE+ " " + Build.VERSION.INCREMENTAL + " " + Build.SERIAL);
            Log.d("Model version ",Build.MANUFACTURER + " " + Build.MODEL + " " + Build.DEVICE+ " " + Build.VERSION.INCREMENTAL + " " + Build.SERIAL);

            val manufacturer=Build.MANUFACTURER
            val model=Build.MODEL
            val isFireOs:Boolean=manufacturer.equals("AMAZON",ignoreCase = true) &&
                    model.startsWith("Fire",ignoreCase = true)
            return if(isFireOs)
                "FireOS"
            else
                "AndroidOS"
        } catch (e: Exception) {
            generateDialog(e.message!!,"Exception GetOSName")
        }
            return ""
    }
    private fun generateAmazonToken(){
        try {
            Timber.d("Generate Amazon Token called")
            val onInitCallback: OnInitCallback = object : OnInitCallback() {
                override fun onReady(initCallbackResponse: InitCallbackResponse) {
                    if (initCallbackResponse.isSuccessful) {
                        generateDialog(initCallbackResponse.token,"Amazon Token")
                        Timber.d("Registration id generated ${initCallbackResponse.token}")
                    } else {
                        Timber.e("Registration failed with Error: " +
                                initCallbackResponse.errorMessage)
                        generateDialog(initCallbackResponse.errorMessage,"Amazon Token error")
                    }
                }
            }
            A3LMessaging.init(applicationContext, onInitCallback)
        }
        catch (register:RuntimeException){
            generateDialog(register.message!!,"Amazon Token")
        }
        catch (e: Exception) {
            generateDialog(e.message!!,"Amazon Token")
            Timber.e(e)
        }

        //Util.validateServerConfigurations(this)
    }
    private fun generateDialog(message:String, title:String){
        val dialog = AlertDialog.Builder(this).setTitle(title)
            .setMessage(message).
                setPositiveButton("Ok",DialogInterface.OnClickListener{dialog,id->
                   dialog.dismiss()
                }).
            create()
       dialog.show()
    }
    private fun generateToken(){
        try {
            Timber.d("Firebase device token trigger")
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.d("Firebase device token trigger error ${task.exception}")
                    return@OnCompleteListener
                }
                else {
                    Timber.d("Firebase device token trigger ${task.result}")
                      generateDialog(task.result,"Firebase Token")
                }

            })
        } catch (e: Exception) {
            generateDialog(e.message!!,"Exception")
        }

    }
    private fun registerADM() {
    }

    private fun hasWritePermission(): Boolean {
        val writePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        return ContextCompat.checkSelfPermission(this, writePermission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestWritePermission() {
        val writePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(this, arrayOf(writePermission), REQUEST_WRITE_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (requestCode == REQUEST_WRITE_PERMISSION && grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val fileLoggingTree=FileLoggingTree(this)
                    Timber.plant(fileLoggingTree)
                } else {
                    generateDialog("Request not granted","Error")
                }
            }
        } catch (e: Exception) {
            generateDialog(e.message!!,"Exception")
        }
    }
}