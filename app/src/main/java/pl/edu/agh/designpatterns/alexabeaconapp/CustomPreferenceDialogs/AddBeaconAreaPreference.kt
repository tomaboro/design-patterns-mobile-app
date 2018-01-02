package pl.edu.agh.designpatterns.alexabeaconapp.CustomPreferenceDialogs;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import pl.edu.agh.designpatterns.alexabeaconapp.*

import kotlinx.android.synthetic.main.dialog_addbeaconarea.*
import kotlinx.android.synthetic.main.elem_beaconarea.*
import pl.edu.agh.designpatterns.alexabeaconapp.BeaconArea
import pl.edu.agh.designpatterns.alexabeaconapp.R


/**
 * Created by motek on 02.01.18.
 */

class AddBeaconAreaPreference(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {

    init {
        dialogLayoutResource = R.layout.dialog_addbeaconarea
        setPositiveButtonText(android.R.string.ok)
        setNegativeButtonText(android.R.string.cancel)
        dialogIcon = null
    }

    override fun onCreateDialogView(): View {
        return super.onCreateDialogView()
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if(positiveResult) {

            //persistBeaconArea(BeaconArea(dialog.aba_area_tag_et.text.toString(),dialog.aba_area_radius_et.toString().toDouble()))
            persistBeaconArea(BeaconArea("asd",5.0))
        }
    }
}
