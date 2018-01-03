package pl.edu.agh.designpatterns.alexabeaconapp

import android.content.Context
import android.preference.DialogPreference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by motek on 02.01.18.
 */

data class BeaconArea(val tag: String, val radius: Double, val image: Int)


fun DialogPreference.getBeaconAreas() : MutableList<BeaconArea> {
    val sharedPreferences = context.getSharedPreferences("To DO", Context.MODE_PRIVATE)
    val areasListJSON = sharedPreferences.getString(context.getString(R.string.pref_key_beacons_areas),"")
    val beaconAreasListType = object : TypeToken<MutableList<BeaconArea>>(){}.type
    return Gson().fromJson<MutableList<BeaconArea>>(areasListJSON,beaconAreasListType)
}

fun DialogPreference.removeBeaconArea(position: Int) {
    val areasList = getBeaconAreas().removeAt(position)

    val newAreasListJSON = Gson().toJson(areasList)
    val sharedPreferencesEditor = sharedPreferences.edit()

    sharedPreferencesEditor.putString(context.getString(R.string.pref_key_beacons_areas),newAreasListJSON)

    sharedPreferencesEditor.apply()
}

fun DialogPreference.persistBeaconArea(beaconArea: BeaconArea) {
    var areasList = getBeaconAreas()

    areasList.add(beaconArea)

    val newAreasListJSON = Gson().toJson(areasList)
    val sharedPreferencesEditor = sharedPreferences.edit()

    sharedPreferencesEditor.putString(context.getString(R.string.pref_key_beacons_areas),newAreasListJSON)

    sharedPreferencesEditor.apply()
}