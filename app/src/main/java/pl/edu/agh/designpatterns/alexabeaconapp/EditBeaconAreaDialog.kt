package pl.edu.agh.designpatterns.alexabeaconapp

import android.app.DialogFragment
import android.R.string.cancel
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.os.Bundle
import pl.edu.agh.designpatterns.alexabeaconapp.EditBeaconAreaDialog.NoticeDialogListener
import android.app.Activity
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_editbeaconarea.*
import kotlinx.android.synthetic.main.dialog_editbeaconarea.view.*


/**
 * Created by motek on 03.01.18.
 */

class EditBeaconAreaDialog() : DialogFragment() {
    var areaTag: String? = null
    var areaRadius: Double? = null
    var areaImage: Int? = null
    var elemIndex: Int = -1

    interface NoticeDialogListener {
        fun onDialogPositiveClick(editBeaconAreaDialog: EditBeaconAreaDialog)
    }

    var mListener: NoticeDialogListener? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mListener = activity as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement NoticeDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_editbeaconarea, null)

        if(elemIndex >= 0) {
            dialogView.eba_area_tag_et.setText(areaTag)
            dialogView.eba_area_radius_et.setText(areaRadius.toString())
        }

        builder.setView(dialogView)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, id ->
                    areaTag = dialogView.eba_area_tag_et.text.toString()
                    areaRadius = dialogView.eba_area_radius_et.text.toString().toDouble()
                    areaImage = R.drawable.ic_launcher_foreground
                    mListener!!.onDialogPositiveClick(this)
                })
                .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel() })
        return builder.create()
    }

    fun collectData(): BeaconArea {
        return BeaconArea(eba_area_tag_et.text.toString(),eba_area_radius_et.text.toString().toDouble(), R.drawable.ic_launcher_foreground)
    }
}