package com.example.kolobanovda_ki23_16_1b_pr2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kolobanovda_ki23_16_1b_pr2.data.DatabaseHelper

class RestaurantActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        dbHelper = DatabaseHelper(this)

        // Навигация
        findViewById<View>(R.id.deliveryAddressText)?.setOnClickListener {
            startActivity(Intent(this, AddressListActivity::class.java))
        }

        findViewById<View>(R.id.homeButton)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<View>(R.id.cartButton)?.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<View>(R.id.profileButton)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Переход к деталям продуктов (Activity Food)
        val foodItemIds = listOf(
            R.id.foodItemGumburger,
            R.id.foodItemFishburger,
            R.id.foodItemCheeseburger,
            R.id.foodItemChicken,
            R.id.foodItemFries,
            R.id.foodItemNuggets
        )

        foodItemIds.forEach { id ->
            findViewById<View>(id)?.setOnClickListener {
                startActivity(Intent(this, FoodActivity::class.java))
            }
        }

        setupAddButton(R.id.addGumburger, "Гамбургер", "99.99", "108 г", R.drawable.gumburger)
        setupAddButton(R.id.addFishburger, "Фиш Бургер", "209.99", "168 г", R.drawable.fishburger)
        setupAddButton(R.id.addCheeseburger, "Чизбургер", "109.99", "114 г", R.drawable.cheeseburger)
        setupAddButton(R.id.addChickenburger, "Чикенбургер", "119.99", "137 г", R.drawable.chickenburger)
        setupAddButton(R.id.addFries, "Картофель фри", "159.99", "100 г", R.drawable.fries)
        setupAddButton(R.id.addNuggets, "Наггетсы", "209.99", "124 г", R.drawable.nuggets)
    }

    private fun setupAddButton(viewId: Int, name: String, price: String, weight: String, imageRes: Int) {
        findViewById<View>(viewId)?.setOnClickListener {
            val id = dbHelper.addToCart(name, price, 1, weight, imageRes)
            if (id != -1L) {
                Toast.makeText(this, "$name добавлен в корзину!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
