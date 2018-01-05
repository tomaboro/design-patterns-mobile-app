package pl.edu.agh.designpatterns.alexabeaconapp

import android.os.Bundle
import android.content.SharedPreferences
import android.preference.*
import android.util.Log


/**
 * Created by motek on 05.01.18.
 */

class SettingsFragment() : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.preferences)

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    override fun onResume() {
        super.onResume()
        for (i in 0 until preferenceScreen.preferenceCount) {
            val preference = preferenceScreen.getPreference(i)
            if (preference is PreferenceGroup) {
                for (j in 0 until preference.preferenceCount) {
                    val singlePref = preference.getPreference(j)
                    updatePreference(singlePref)
                }
            } else {
                updatePreference(preference)
            }
        }
    }

    private fun updatePreference(preference: Preference?) {
        if (preference == null) return
        else if (preference is EditTextPreference) {
            val etPreference = preference as EditTextPreference?
            if(etPreference!!.text.isBlank()) etPreference.summary = "Unset"
            else etPreference.summary = etPreference.text
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        updatePreference(findPreference(p1))
    }
}