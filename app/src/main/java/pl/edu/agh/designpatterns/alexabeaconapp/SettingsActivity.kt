package pl.edu.agh.designpatterns.alexabeaconapp


import android.app.Activity
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity

/**
 * Created by motek on 02.01.18.
 */

class SettingsActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment.newInstance())
                .commit()
    }
}
