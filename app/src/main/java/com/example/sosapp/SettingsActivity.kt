package com.example.sosapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val phoneInput = findViewById<EditText>(R.id.phoneInput)
        val saveButton = findViewById<Button>(R.id.saveBtn)

        val prefs = getSharedPreferences("sos", MODE_PRIVATE)
        phoneInput.setText(prefs.getString("number", "112"))

        saveButton.setOnClickListener {
            val number = phoneInput.text.toString().trim()
            if (number.isNotEmpty()) {
                prefs.edit().putString("number", number).apply()
                finish()
            }
        }
    }
}
