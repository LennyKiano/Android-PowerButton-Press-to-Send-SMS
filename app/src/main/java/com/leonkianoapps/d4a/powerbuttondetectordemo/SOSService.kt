package com.leonkianoapps.d4a.powerbuttondetectordemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.IntentFilter
import android.content.BroadcastReceiver
import android.util.Log


class SOSService : Service() {

    var sosBroadcastReceiver: SOSBroadcastReceiver? = null
    var intentFilter: IntentFilter? = null

    override fun onBind(intent: Intent?): IBinder? {


        return null
    }


    override fun onCreate() {

        intentFilter = IntentFilter()
        intentFilter?.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter?.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter?.addAction(Intent.ACTION_BOOT_COMPLETED)

        // Set broadcast receiver priority.
        intentFilter?.priority = 100

        sosBroadcastReceiver = SOSBroadcastReceiver()

        registerReceiver(sosBroadcastReceiver,intentFilter)

        Log.d("SERVICE","success Register")

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()


//        if(sosBroadcastReceiver != null){
//
//            unregisterReceiver(sosBroadcastReceiver)
//            Log.d("SERVICE"," UNRegister")
//
//        }
    }


}