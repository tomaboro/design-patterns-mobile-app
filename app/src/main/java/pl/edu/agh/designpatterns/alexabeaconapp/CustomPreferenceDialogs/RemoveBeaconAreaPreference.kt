package pl.edu.agh.designpatterns.alexabeaconapp.CustomPreferenceDialogs

import android.content.Context
import android.content.SharedPreferences
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.dialog_removebeaconarea.*
import kotlinx.android.synthetic.main.dialog_removebeaconarea.view.*
import kotlinx.android.synthetic.main.elem_beaconarea.*
import kotlinx.android.synthetic.main.elem_beaconarea.view.*
import pl.edu.agh.designpatterns.alexabeaconapp.BeaconArea
import pl.edu.agh.designpatterns.alexabeaconapp.R
import pl.edu.agh.designpatterns.alexabeaconapp.*
import android.support.v4.content.ContextCompat.startActivity
import android.app.Activity
import android.content.Intent
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.support.v4.content.ContextCompat.startActivity
import android.provider.AlarmClock.EXTRA_MESSAGE



/**
 * Created by motek on 02.01.18.
 */

class RowAdapter(context: Context?, resource: Int, list: MutableList<BeaconArea>?) : ArrayAdapter<BeaconArea>(context, resource,list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.elem_beaconarea, parent, false)

        rowView.area_tag.text = getItem(position).tag
        rowView.area_radius.text = getItem(position).radius.toString()

        return rowView
    }
}

class RemoveBeaconAreaPreference(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {
    var listOfAreas = getBeaconAreas()

    init {
        dialogLayoutResource = R.layout.dialog_removebeaconarea
        setNegativeButtonText(android.R.string.cancel)
        dialogIcon = null
        //dialog.beacons_areas_lv.adapter = RowAdapter(context,R.layout.elem_beaconarea,listOfAreas)

        //dialog.beacons_areas_lv.setOnItemClickListener({ parent, view, position, id ->
        //    removeBeaconArea(position)
        //    dialog.cancel()
        //})
    }

}