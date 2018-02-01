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
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_addrule.*
import kotlinx.android.synthetic.main.dialog_addrule.view.*
import android.R.array
import android.content.SharedPreferences
import android.os.Build
import android.support.annotation.RequiresApi


/**
 * Created by motek on 03.01.18.
 */

class AddRuleDialog() : DialogFragment() {
    var question: String? = "Question"
    var response: String? = "Response"
    var beacon: String? = "Beacon"

    interface NoticeDialogListener {
        fun onDialogPositiveClick(addRuleDialog: AddRuleDialog)
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
        val dialogView = inflater.inflate(R.layout.dialog_addrule, null)

        dialogView.ar_beacon_title.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onClick(v: View) {
                val areas = sharedPreferancesAdapter(context).getBeaconAreas()
                val names = areas.map {area -> area.name  }
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Picka Area")
                        .setItems(names.toTypedArray(), DialogInterface.OnClickListener { dialog, which ->
                            dialogView.ar_beacon_title.text = names.get(which)
                        })
                builder.create().show()
            }
        })

        builder.setView(dialogView)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, id ->
                    question = dialogView.ar_question.text.toString()
                    response = dialogView.ar_response.text.toString()
                    beacon = dialogView.ar_beacon_title.text.toString()
                    mListener!!.onDialogPositiveClick(this)
                })
                .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel() })
        return builder.create()
    }
}