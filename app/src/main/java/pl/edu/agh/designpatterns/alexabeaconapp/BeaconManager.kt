package pl.edu.agh.designpatterns.alexabeaconapp

import android.app.Notification
import android.content.Context
import android.util.DebugUtils
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.estimote.cloud_plugin.common.EstimoteCloudCredentials
import com.estimote.proximity_sdk.proximity.ProximityObserver
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder
import com.estimote.proximity_sdk.proximity.ProximityZone
import org.json.JSONObject

/**
 * Created by motek on 03.01.18.
 */

class BeaconManager(val context: Context) {

    var areasList = mutableListOf<ProximityZone>()

    val cloudCredentials = EstimoteCloudCredentials("design-patterns-app-7am" , "2cc603a3c8ffa027140a576f4de11d56")

    val notification = NotificationCreator().create(context)

    val proximityObserver = ProximityObserverBuilder(context, cloudCredentials)
            .withBalancedPowerMode()
            .withOnErrorAction { /* handle errors here */ }
            .withScannerInForegroundService(notification)
            .build()
    val sharedPreferencesAdapter = sharedPreferancesAdapter(context)

    var observationHandler : ProximityObserver.Handler? = null


    fun refreshAreas() {
        areasList.clear()

        sharedPreferencesAdapter.getBeaconAreas().forEach { beaconArea ->
            val nxtZone = proximityObserver.zoneBuilder()
                    .forAttachmentKeyAndValue("venuess", beaconArea.tag)
                    .inCustomRange(beaconArea.radius)
                    .withOnEnterAction {
                        Log.d("Connection","wszedles")
                        val url = context.getSharedPreferences("To DO",Context.MODE_PRIVATE).getString(context.getString(R.string.pref_key_others_server_url),"https://2318e87e.ngrok.io") + "/user"
                        val name = beaconArea.name
                        val id = context.getSharedPreferences("To DO",Context.MODE_PRIVATE).getString(context.getString(R.string.pref_key_alexa_username),"0")
                        val jsonBody = JSONObject("{\"id\":\"" + id + "\",\"beacon\":\"" + name + " \"}")
                        Log.d("STH",url+ "    ")
                        val stringRequest = JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                                object : Response.Listener<JSONObject> {
                                    override fun onResponse(response: JSONObject?) {
                                        Log.d("Connection","Success")
                                    }
                                },
                                object : Response.ErrorListener {
                                    override fun onErrorResponse(error: VolleyError) {
                                        Log.d("Connection","Error")
                                    }
                                })

                        ConnectionSingleton.getInstance(this.context).addToRequestQueue(stringRequest)

                    }
                    .withOnExitAction {
                        Log.d("Connection","wyszedles")
                        val url = context.getSharedPreferences("To DO",Context.MODE_PRIVATE).getString(context.getString(R.string.pref_key_others_server_url),"https://2318e87e.ngrok.io") + "/user"
                        val name = beaconArea.name
                        val id = context.getSharedPreferences("To DO",Context.MODE_PRIVATE).getString(context.getString(R.string.pref_key_alexa_username),"0")
                        Log.d("debug","{\"id\":\"" + id + "\",\"beacon\":\"" + name + " \"}")
                        val jsonBody = JSONObject("{\"id\":\"" + id + "\",\"beacon\":\"" + name + " \"}")

                        val stringRequest = JsonObjectRequest(Request.Method.POST, url, jsonBody,
                                object : Response.Listener<JSONObject> {
                                    override fun onResponse(response: JSONObject?) {
                                        Log.d("Connection","Success")
                                    }
                                },
                                object : Response.ErrorListener {
                                    override fun onErrorResponse(error: VolleyError) {
                                        Log.d("Connection","Error")
                                    }
                                })

                        ConnectionSingleton.getInstance(this.context).addToRequestQueue(stringRequest)
                    }
                    .create()

            areasList.add(nxtZone)

        }
    }

    fun startScanning() {

        refreshAreas()

        areasList.forEach{ nxtZone ->
            Log.d("Tag",nxtZone.attachmentKey + ", " + nxtZone.attachmentValue + ", " + nxtZone.desiredMeanTriggerDistance)
        }

        observationHandler = proximityObserver
                .addProximityZones(areasList.toList())
                .start()

    }

    fun stopScanning() {
        observationHandler?.stop()
    }

}