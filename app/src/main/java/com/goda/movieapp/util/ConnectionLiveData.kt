package com.goda.movieapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import com.goda.movieapp.data.ConnectionModel

class ConnectionLiveData(val context: Context) : LiveData<ConnectionModel>() {



    override fun onActive() {
        super.onActive()
        val filter =
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context!!.unregisterReceiver(networkReceiver)
    }

    private val networkReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                if (intent.extras != null) {
                    val notConnected = intent.getBooleanExtra(
                        ConnectivityManager
                            .EXTRA_NO_CONNECTIVITY, false
                    )
                    if (!notConnected/*context.isOnline()*/ /* context.getConnectionType() !in 1..2*/) {
                        postValue(
                            ConnectionModel(
                                true
                            )
                        )
                    } else {
                        postValue(ConnectionModel(false))
                    }
                }
            }
        }
}
