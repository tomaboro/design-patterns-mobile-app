package pl.edu.agh.designpatterns.alexabeaconapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.estimote.cloud_plugin.common.EstimoteCloudCredentials
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.proximity.ProximityObserver
import com.estimote.proximity_sdk.proximity.ProximityZone

import kotlinx.android.synthetic.main.activity_main.*
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import retrofit2.http.GET
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject


class MainActivity : AppCompatActivity(), AddRuleDialog.NoticeDialogListener {
    override fun onDialogPositiveClick(addRuleDialog: AddRuleDialog) {
        val url = getSharedPreferences("To DO",Context.MODE_PRIVATE).getString(getString(R.string.pref_key_others_server_url),"https://2318e87e.ngrok.io") + "/question"
        val jsonBody = JSONObject("{\"question\":\"" + addRuleDialog.question + "\",\"answer\":\"" + addRuleDialog.response + "\",\"beaconID\":\"" + addRuleDialog.beacon + " \"}")

        val stringRequest = JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {
                        Log.d("Connection","Success")
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Connection","Error")
                    }
                })

        ConnectionSingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val bm = BeaconManager(this)

        bm.refreshAreas()

        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
                this,
                onRequirementsFulfilled = {
                    bm.startScanning()
                },
                onRequirementsMissing =  { Log.i("DEBUG","miss")},
                onError = { Log.i("DEBUG","err")}
        )

        fab.setOnClickListener { view ->
            val newFragment = AddRuleDialog()
            newFragment.show(this.fragmentManager,"sth345")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this,SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
