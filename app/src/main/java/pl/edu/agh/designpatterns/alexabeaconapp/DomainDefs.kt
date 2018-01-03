package pl.edu.agh.designpatterns.alexabeaconapp

import android.content.Context
import android.preference.DialogPreference
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.IllegalStateException

/**
 * Created by motek on 02.01.18.
 */

data class BeaconArea(val name: String, val tag: String, val radius: Double)

class sharedPreferancesAdapter(val context: Context) {
    fun getBeaconAreas(): MutableList<BeaconArea> {
        val sharedPreferences = context.getSharedPreferences("To DO", Context.MODE_PRIVATE)
        val areasListJSON = sharedPreferences.getString(context.getString(R.string.pref_key_beacons_areas), "")
        val beaconAreasListType = object : TypeToken<MutableList<BeaconArea>>() {}.type
        return try {
            val ret = Gson().fromJson<MutableList<BeaconArea>>(areasListJSON, beaconAreasListType)
            ret
        }catch (e: IllegalStateException) {
            Toast.makeText(context,context.resources.getText(R.string.reading_preferances_error),Toast.LENGTH_SHORT).show()
            mutableListOf<BeaconArea>()
        }
    }

    fun removeBeaconArea(position: Int) {
        val sharedPreferences = context.getSharedPreferences("To DO", Context.MODE_PRIVATE)
        val areasList = getBeaconAreas().removeAt(position)
        val newAreasListJSON = Gson().toJson(areasList)
        val sharedPreferencesEditor = sharedPreferences.edit()

        sharedPreferencesEditor.putString(context.getString(R.string.pref_key_beacons_areas), newAreasListJSON)

        sharedPreferencesEditor.apply()
    }

    fun persistBeaconArea(beaconArea: BeaconArea) {
        val sharedPreferences = context.getSharedPreferences("To DO", Context.MODE_PRIVATE)
        var areasList = getBeaconAreas()
        areasList.add(beaconArea)
        val newAreasListJSON = Gson().toJson(areasList)
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.putString(context.getString(R.string.pref_key_beacons_areas), newAreasListJSON)
        sharedPreferencesEditor.apply()
    }
}