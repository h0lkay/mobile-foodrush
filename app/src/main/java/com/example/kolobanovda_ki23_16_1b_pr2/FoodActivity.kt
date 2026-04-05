package com.example.kolobanovda_ki23_16_1b_pr2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kolobanovda_ki23_16_1b_pr2.data.DatabaseHelper

class FoodActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        dbHelper = DatabaseHelper(this)

        val addToCartButton = findViewById<Button>(R.id.addToCartButton)
        
        addToCartButton?.setOnClickListener {
            val foodNameView = findViewById<TextView>(R.id.foodNameBottom)
            val foodName = foodNameView?.text?.toString() ?: "Чикенбургер"
            
            val foodPrice = "119.99"
            val foodWeight = "137 г"
            val foodImage = R.drawable.chickenburger
            
            val id = dbHelper.addToCart(foodName, foodPrice, 1, foodWeight, foodImage)
            
            if (id != -1L) {
                startActivity(Intent(this, CartActivity::class.java))
            } else {
                Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<View>(R.id.closeButton)?.setOnClickListener {
            finish()
        }
    }
}
