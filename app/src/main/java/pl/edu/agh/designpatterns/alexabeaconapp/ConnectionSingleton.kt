package pl.edu.agh.designpatterns.alexabeaconapp

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog.TAG
import com.android.volley.toolbox.Volley


/**
 * Created by motek on 04.01.18.
 */

class ConnectionSingleton private constructor(val context: Context){

    companion object {
        private var mInstance: ConnectionSingleton? = null
        @Synchronized
        fun getInstance(context: Context): ConnectionSingleton {
            if (mInstance == null) {
                mInstance = ConnectionSingleton(context)
            }
            return mInstance as ConnectionSingleton
        }
    }

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(context)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }

    fun cancelPendingRequests(tag: Any) {
        if (requestQueue != null) {
            requestQueue!!.cancelAll(tag)
        }
    }
}
