package com.example.sosapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sosButton = findViewById<Button>(R.id.sosButton)
        val settingsIcon = findViewById<ImageView>(R.id.settingsIcon)

        sosButton.setOnClickListener {
            sendSOS()
        }

        settingsIcon.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            sendSOS()
        }

    private fun sendSOS() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        val prefs = getSharedPreferences("sos", MODE_PRIVATE)
        val phoneNumber = prefs.getString("number", "112")!!

        val fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        fusedLocation.lastLocation.addOnSuccessListener { location ->
            val locationText = if (location != null) {
                "https://maps.google.com/?q=${location.latitude},${location.longitude}"
            } else {
                "Location unavailable"
            }

            val smsIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.parse("smsto:$phoneNumber")
            )
            smsIntent.putExtra(
                "sms_body",
                "SOS! I need help. My location: $locationText"
            )
            startActivity(smsIntent)

            val callIntent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:$phoneNumber")
            )
            startActivity(callIntent)
        }
    }
}
