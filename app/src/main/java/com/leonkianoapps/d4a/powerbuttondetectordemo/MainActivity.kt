package com.leonkianoapps.d4a.powerbuttondetectordemo

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.Manifest
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val appName = "PBDD"

    val SMS_REQUEST_CODE: Int = 111

    var count = 0

    var SOS_NUMBER = "+254704707590" //spare line

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(appName, "In OnCreate")
        //check permissions for android API 23 (Marshmallow) and greater

        checkPerm()

        registerButton.setOnClickListener { registerSOSNumber() }


        val backgroundService = Intent(applicationContext, SOSService::class.java)
        startService(backgroundService)


    }

    private fun checkPerm() {

        //if phone is greater than or is Marshmallow show runtime permissions

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.SEND_SMS),
                    SMS_REQUEST_CODE
                )
            }


        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {

            SMS_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(appName, "Permission has been denied by user")
                    toast("Permission has been denied")

                } else {


                    Log.i(appName, "Permission has been granted by user")
                    toast("Permission has been granted")

                }


            }


        }

    }


    private fun toast(message: String) {

        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

    }


    private fun registerSOSNumber() {


        //in case a user disabled the sms feature
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.SEND_SMS),
                    SMS_REQUEST_CODE
                )
            } else {


                reg()

            }

        } else {

            reg()


        }


    }


    private fun reg() {


        if (TextUtils.isEmpty(number_TextField.text.toString())) {

            val numberIntent = Intent("number.action.string")
            //put default number

            numberIntent.putExtra("number", SOS_NUMBER)

            sendBroadcast(numberIntent)

            val message = "The default app number was registered"
            showSnackMessage(message)


        } else {

            val userNumber = number_TextField.text.toString()

            if (userNumber.length == 10 || userNumber.length <= 13) {

                val numberIntent = Intent("number.action.string")
                //put user number

                numberIntent.putExtra("number", SOS_NUMBER)

                sendBroadcast(numberIntent)

                val message = "The $userNumber  number was registered"
                showSnackMessage(message)

            } else {

                val message = "Please enter a valid number and try again"
                showSnackMessage(message)


            }


        }


    }

    private fun showSnackMessage(message: String) {

        val snackBar = Snackbar.make(main_ui_layout, message, Snackbar.LENGTH_LONG)
        snackBar.setAction("OKAY", View.OnClickListener { snackBar.dismiss() })
        snackBar.show()


    }


}
