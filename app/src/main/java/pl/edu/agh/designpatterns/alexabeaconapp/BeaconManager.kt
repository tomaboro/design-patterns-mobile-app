package pl.edu.agh.designpatterns.alexabeaconapp

import android.app.Notification
import android.content.Context
import android.util.DebugUtils
import android.util.Log
import android.widget.Toast
import com.estimote.cloud_plugin.common.EstimoteCloudCredentials
import com.estimote.proximity_sdk.proximity.ProximityObserver
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder
import com.estimote.proximity_sdk.proximity.ProximityZone

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
            areasList.add(
                    proximityObserver.zoneBuilder()
                    .forAttachmentKeyAndValue("area", beaconArea.tag)
                    .inCustomRange(beaconArea.radius)
                            .withOnEnterAction {Log.d("beacon","enter: " + beaconArea.name) }
                            .withOnExitAction { Log.d("beacon","exit: " + beaconArea.name) }
                    .create()
            )
        }
    }

    fun startScanning() {

        val mintDeskZone = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("desk", "mint")
                .inNearRange()
                .create()

        areasList.add(mintDeskZone)

        observationHandler = proximityObserver
                .addProximityZones(areasList.toList())
                .start()

        Log.d("2123","asdasdads")
    }

    fun stopScanning() {
        observationHandler?.stop()
    }

}