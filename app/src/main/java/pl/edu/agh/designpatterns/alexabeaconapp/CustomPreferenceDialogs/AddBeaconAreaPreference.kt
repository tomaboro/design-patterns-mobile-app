package pl.edu.agh.designpatterns.alexabeaconapp.CustomPreferenceDialogs;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import pl.edu.agh.designpatterns.alexabeaconapp.R;


/**
 * Created by motek on 02.01.18.
 */

enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}

class BeaconArea(tag: String, radius: Double, color: Color)

fun DialogPreference.persistBeaconArea(beaconArea: BeaconArea) {
    val sharedPreferences = context.getSharedPreferences("To DO",Context.MODE_PRIVATE)
    val areasListJSON = sharedPreferences.getString(context.getString(R.string.pref_key_beacons_areas),"")
    val beaconAreasListType = object : TypeToken<MutableList<BeaconArea>>(){}.type
    var areasList = Gson().fromJson<MutableList<BeaconArea>>(areasListJSON,beaconAreasListType)

    if(areasList == null) areasList = mutableListOf<BeaconArea>()

    areasList.add(beaconArea)

    val newAreasListJSON = Gson().toJson(areasList)
    val sharedPreferencesEditor = sharedPreferences.edit()

    sharedPreferencesEditor.putString(context.getString(R.string.pref_key_beacons_areas),newAreasListJSON)

    sharedPreferencesEditor.apply()
}

class AddBeaconAreaPreference(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {

    init {
        setDialogLayoutResource(R.layout.dialog_addbeaconarea)
        setPositiveButtonText(android.R.string.ok)
        setNegativeButtonText(android.R.string.cancel)
        setDialogIcon(null)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if(positiveResult) {
            persistBeaconArea(BeaconArea("tmp",5.0, Color.BLUE))
        }
    }
}
