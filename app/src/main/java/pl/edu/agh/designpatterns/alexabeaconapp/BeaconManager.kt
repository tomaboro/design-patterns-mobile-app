package pl.edu.agh.designpatterns.alexabeaconapp

import android.app.Notification
import android.content.Context
import android.util.DebugUtils
import android.util.Log
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
                    .forAttachmentKeyAndValue("venue", beaconArea.name)
                    .inCustomRange(beaconArea.radius)
                    .create())
        }
    }

    fun startScanning() {
        val venueZone = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("venue", "office")
                .inCustomRange(5.0)
                .create()

        // The next zone is defined for a single desk in your venue - let's call it "Mint desk".
        val mintDeskZone = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("desk", "mint")
                .inNearRange()
                .create()

        // The last zone is defined for another single desk in your venue - the "Blueberry desk".
        val blueberryDeskZone = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("desk", "blueberry")
                .inNearRange()
                .create()

        Log.d("DEBUG",areasList.toString())

        areasList.add(venueZone)

        observationHandler = proximityObserver
                .addProximityZones(areasList.toList())
                .start()
    }

    fun stopScanning() {
        observationHandler?.stop()
    }

}