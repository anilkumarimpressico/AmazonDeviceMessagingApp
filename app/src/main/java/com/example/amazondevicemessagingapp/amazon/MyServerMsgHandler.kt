package com.example.amazondevicemessagingapp.amazon

import android.content.Context
import android.util.Log
import com.example.amazondevicemessagingapp.R

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MyServerMsgHandler {
    /** Tag for logs. */
    private val TAG = "ADMMessenger"

    /** The server action "/register" sends the app instance's unique registration ID to your server. */
    private val REGISTER_ROUTE = "/register"

    /** The server action "/unregister" notifies your server that this app instance is no longer registered with ADM. */
    private val UNREGISTER_ROUTE = "/unregister"

    /**
     * Sends an asynchronous http request to your server, to avoid blocking the main thread.
     *
     * @param context Your application's current context.
     * @param httpRequest The http request to send.
     */
    private fun sendHttpRequest(context: Context, httpRequest: String) {
        Log.i(TAG, "Sending http request $httpRequest")
        val client = OkHttpClient()
        val request = Request.Builder().url(httpRequest).build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                response.close()

                responseBody?.let {
                    /* Log your server's response */
                    Log.i(TAG, it)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
            }
        }
    }

    /**
     * Sends the app instance's unique registration ID to your server.
     *
     * @param context Your application's current context.
     * @param registrationId Your application instance's registration ID.
     */
    fun registerAppInstance(context: Context, registrationId: String) {
        Log.i(TAG, "Sending registration id to 3rd party server $registrationId")

        /* Build the URL to address your server. Values for server_address and server_port must be set correctly in your string.xml file. */
        val serverAddress = context.getString(R.string.server_address)
        val serverPort = context.getString(R.string.server_port)
        val baseUrl = "$serverAddress:$serverPort"

        /* Create Retrofit instance */
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        /* Create service interface */
        val service = retrofit.create(MyServerService::class.java)

        /* Build the full URL and send the registration request asynchronously */
        val fullUrl = "$baseUrl$REGISTER_ROUTE?device=$registrationId"
        sendHttpRequest(context, fullUrl)
    }

    /**
     * Notifies your server that this app instance is no longer registered with ADM.
     *
     * @param context Your application's current context.
     * @param registrationId Your application instance's registration ID.
     */
    fun unregisterAppInstance(context: Context, registrationId: String) {
        Log.i(TAG, "Sending unregistration id to 3rd party server $registrationId")

        /* Build the URL to address your server. Values for server_address and server_port must be set correctly in your string.xml file. */
        val serverAddress = context.getString(R.string.server_address)
        val serverPort = context.getString(R.string.server_port)
        val baseUrl = "$serverAddress:$serverPort"

        /* Create Retrofit instance */
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        /* Create service interface */
        val service = retrofit.create(MyServerService::class.java)

        /* Build the full URL and send the unregister request asynchronously */
        val fullUrl = "$baseUrl$UNREGISTER_ROUTE?device=$registrationId"
        sendHttpRequest(context, fullUrl)
    }
}

interface MyServerService{

}
