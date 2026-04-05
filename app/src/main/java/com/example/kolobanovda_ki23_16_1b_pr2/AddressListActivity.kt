package com.example.kolobanovda_ki23_16_1b_pr2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kolobanovda_ki23_16_1b_pr2.data.DatabaseHelper

class AddressListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        dbHelper = DatabaseHelper(this)

        refreshAddressList()

        findViewById<View>(R.id.btnNewAddress)?.setOnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        }

        findViewById<View>(R.id.backBtn)?.setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.btnDeliverHere)?.setOnClickListener {
            if (selectedPosition != -1) {
                Toast.makeText(this, "Адрес выбран!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Пожалуйста, выберите адрес", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshAddressList()
    }

    private fun refreshAddressList() {
        val addresses = dbHelper.getAllAddresses()
        val container = findViewById<LinearLayout>(R.id.addressContainer)
        container?.removeAllViews()

        val inflater = LayoutInflater.from(this)
        val radioButtons = mutableListOf<RadioButton>()

        addresses.forEachIndexed { index, address ->
            val itemView = inflater.inflate(R.layout.item_address, container, false)
            val text = "${address.street}, ${address.city}"
            itemView.findViewById<TextView>(R.id.addressText).text = text
            
            val rb = itemView.findViewById<RadioButton>(R.id.addressRadioButton)
            radioButtons.add(rb)

            itemView.findViewById<View>(R.id.btnDeleteAddress).setOnClickListener {
                dbHelper.deleteAddress(address.id)
                refreshAddressList()
                Toast.makeText(this, "Адрес удален", Toast.LENGTH_SHORT).show()
            }

            val clickListener = View.OnClickListener {
                selectedPosition = index
                radioButtons.forEachIndexed { i, radioButton ->
                    radioButton.isChecked = (i == index)
                }
            }

            itemView.setOnClickListener(clickListener)
            rb.setOnClickListener(clickListener)

            container?.addView(itemView)
        }
    }
}
