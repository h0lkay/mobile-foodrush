package com.example.kolobanovda_ki23_16_1b_pr2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.deliveryAddressText)?.setOnClickListener {
            startActivity(Intent(this, AddressListActivity::class.java))
        }

        findViewById<View>(R.id.misterBurgerCard)?.setOnClickListener {
            startActivity(Intent(this, RestaurantActivity::class.java))
        }

        findViewById<View>(R.id.cartButton)?.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<View>(R.id.profileButton)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
