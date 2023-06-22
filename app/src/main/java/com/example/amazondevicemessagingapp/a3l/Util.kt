package com.example.amazondevicemessagingapp.a3l

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.amazondevicemessagingapp.MainActivity
import java.io.IOException
import java.io.InputStream

object Util {
    var ADM_Configuration_file: String? = null
    var FCM_Configuration_file: String? = null
    var token: String? = null

    @Throws(InvalidServerConfigurationException::class)
        fun validateServerConfigurations(context: Context) {
            try {
                val applicationInfo = context.packageManager
                    .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                ADM_Configuration_file =
                    applicationInfo.metaData.getString("ADM_SERVER_CONFIG_FILE_NAME")
                FCM_Configuration_file =
                    applicationInfo.metaData.getString("FCM_SERVER_CONFIG_FILE_NAME")
                if (ADM_Configuration_file == null || ADM_Configuration_file!!.length == 0 || FCM_Configuration_file == null || FCM_Configuration_file?.length == 0) {
                    generateDialog(context,"ADM_Configuration_file or " +
                            "FCM_Configuration_file not provided in manifest.","Error")
                    throw InvalidServerConfigurationException(
                        "ADM_Configuration_file or " +
                                "FCM_Configuration_file not provided in manifest."
                    )
                }
                val errorMessageBuilder = StringBuilder()
                val assetManager = context.assets
                if (!isAssetExists(
                        context,
                        ADM_Configuration_file!!
                    )
                ) {
                    errorMessageBuilder.append(
                        String.format(
                            "ADM Configuration file %s does not exist",
                            ADM_Configuration_file
                        )
                    )
                    generateDialog(context,errorMessageBuilder.toString(),"Error")
                    throw InvalidServerConfigurationException(errorMessageBuilder.toString())
                }
                if (!isAssetExists(
                        context,
                        FCM_Configuration_file!!
                    )
                ) {
                    errorMessageBuilder.append(
                        String.format(
                            "FCM Configuration file %s does not exist",
                            FCM_Configuration_file
                        )
                    )
                    generateDialog(context,errorMessageBuilder.toString(),"Error")
                    throw InvalidServerConfigurationException(errorMessageBuilder.toString())
                }
            } catch (e: PackageManager.NameNotFoundException) {
                generateDialog(context,e.toString(),"Error")
                throw InvalidServerConfigurationException("PackageManager.NameNotFoundException", e)

            }
        }
    private fun generateDialog(context:Context,message:String, title:String){
        val dialog = AlertDialog.Builder(context).setTitle(title)
            .setMessage(message).
            setPositiveButton("Ok",DialogInterface.OnClickListener{dialog,id->
                dialog.dismiss()
            }).
            create()
        dialog.show()
    }
        private fun isAssetExists(context: Context, pathInAssetsDir: String): Boolean {
            val assetManager = context.assets
            var inputStream: InputStream? = null
            try {
                inputStream = assetManager.open(pathInAssetsDir)
                if (null != inputStream) {
                    return true
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    assert(inputStream != null)
                    inputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()

                }
            }
            return false
        }

    }

    class InvalidServerConfigurationException : RuntimeException {
        constructor() : super()
        constructor(message: String) : super(message)
        constructor(message: String, throwable: Throwable) : super(message, throwable)
    }
