package com.leonkianoapps.d4a.powerbuttondetectordemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.support.v7.app.AppCompatActivity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.telephony.SmsManager
import android.view.KeyEvent


class SOSBroadcastReceiver : BroadcastReceiver() {

    private val screenOn = "SCREEN ON"
    private val screenOff = "SCREEN OFF"
    private var count = 0

    private var isTimerRunning = false

    private var timer: CountDownTimer? = null


    private var phoneNumber: String? = "+254704707590"

    override fun onReceive(context: Context?, intent: Intent?) {


        //get SOS number

        val action = intent?.action

        if(action.equals("number.action.string")){

            phoneNumber = intent?.extras?.getString("number")

        }


        //for checking the state of the phone

        when (intent?.action) {


            Intent.ACTION_BOOT_COMPLETED -> {


                //check if timer is running

                if (isTimerRunning) {

                    count++


                } else {

                    count++ //for the first timer

                    //set timer running to true

                    isTimerRunning = true

                    startTimer(context)

                    //to detect if the phone is coming from a boot/restart so we start the service

                    val backgroundService = Intent(context, SOSService::class.java)
                    context?.startService(backgroundService)

                }




            }

            Intent.ACTION_SCREEN_ON -> {

                Log.d("PBDD", screenOn)

                //check if timer is running

                if (isTimerRunning) {

                    count++


                } else {

                    count++ //for the first timer

                    //set timer running to true

                    isTimerRunning = true

                    startTimer(context)

                }


            }

            Intent.ACTION_SCREEN_OFF -> {

                Log.d("PBDD", screenOff)

                //check if timer is running

                if (isTimerRunning) {

                    count++


                } else {

                    count++ //for the first timer

                    //set timer running to true

                    isTimerRunning = true

                    startTimer(context)

                }


            }

        }



    }

    private fun startTimer(context: Context?) {

        Log.i("TIMER", "STARTED")


        timer = object : CountDownTimer(4000, 1000) {

            override fun onTick(p0: Long) {

                //if count within 4 sec is 4 send SOS

                Log.i("TIMER", "TICK")

                if (count == 4) {

                    stopTimer()
                    Log.i("TIMER", "STOPPED SOS FOUND")

                    //send SOS SMS
                    Log.i("SMS", "SENDING...")
                    sendSMS(context)

                    //reset count
                    count = 0

                }

            }

            override fun onFinish() {

                //reset count
                count = 0

                Log.i("TIMER", "FINISHED NO SOS")

                isTimerRunning = false


            }


        }.start()

    }


    private fun stopTimer() {

        isTimerRunning = false

        timer?.cancel()



    }

    private fun sendSMS(context: Context?) {

        //vibrate so that the user knows that the sos was sent successfully

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            vibrator.vibrate(500)
        }


        val smsManager: SmsManager = SmsManager.getDefault()

        smsManager.sendTextMessage(phoneNumber, null, "TEST SOS FROM Leonard Kiano APP", null, null)

    }


}