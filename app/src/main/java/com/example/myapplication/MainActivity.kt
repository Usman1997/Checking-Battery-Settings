package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var powerManager: PowerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager

        val btnBatterySaver = findViewById<Button>(R.id.btnBatterySaver)
        val btnOptimizationSettings = findViewById<Button>(R.id.btnOptimizationSettings)
        val btnIgnoreBatteryOptimizationDirectly = findViewById<Button>(R.id.btnIgnoreBatteryOptimizationDirectly)

        /**
         * ACTION_BATTERY_SAVER_SETTINGS
         * Activity Action: Show battery saver settings.
         * Shows an activity in Samsung which cannot be found with normal settings
         */
        btnBatterySaver.setOnClickListener {
           try {
               val intent = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
               startActivity(intent)
           }catch (e: Exception){
               Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
           }
        }

        /**
         * ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
         * Activity Action: Show screen for controlling which apps can ignore battery optimizations.
         */
        btnOptimizationSettings.setOnClickListener {
            shouldIgnoreBatteryOptimization {
               try {
                   val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                   startActivity(intent)
               }catch (e: Exception){
                   Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
               }
            }
        }

        /**
         * ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
         * Will directly remove the app from battery optimization without navigation to any other screen.
         * But this Intent is not available for every use case and therefore needs permission in Manifest.
         * This can also cause some issues while uploading app on playstore
         */
        btnIgnoreBatteryOptimizationDirectly.setOnClickListener {
            shouldIgnoreBatteryOptimization {
                try {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                    intent.data = Uri.parse("package:${packageName}")
                    startActivity(intent)
                }catch (e: Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun shouldIgnoreBatteryOptimization(callback: () -> Unit) {
        if (powerManager.isIgnoringBatteryOptimizations(packageName)) {
            Toast.makeText(this, "Already Ignoring Optimizing Battery", Toast.LENGTH_LONG).show()
        } else {
            callback()
        }
    }
}
