package com.example.kolobanovda_ki23_16_1b_pr2

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kolobanovda_ki23_16_1b_pr2.data.DatabaseHelper
import com.google.android.material.textfield.TextInputEditText

class AddressActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        dbHelper = DatabaseHelper(this)

        val confirmOrderButton = findViewById<Button>(R.id.confirmOrderButton)
        
        confirmOrderButton?.setOnClickListener {
            val street = findViewById<TextInputEditText>(R.id.streetAddressEditText)?.text.toString().trim()
            val city = findViewById<TextInputEditText>(R.id.cityEditText)?.text.toString().trim()
            val state = findViewById<TextInputEditText>(R.id.stateEditText)?.text.toString().trim()

            if (street.isNotEmpty() && city.isNotEmpty()) {
                val id = dbHelper.addAddress(street, city, state, "")
                if (id != -1L) {
                    Toast.makeText(this, "Адрес сохранен!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Пожалуйста, заполните улицу и город", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
