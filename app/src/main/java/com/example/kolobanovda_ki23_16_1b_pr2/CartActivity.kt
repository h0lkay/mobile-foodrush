package com.example.kolobanovda_ki23_16_1b_pr2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kolobanovda_ki23_16_1b_pr2.data.DatabaseHelper
import java.util.*

class CartActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        dbHelper = DatabaseHelper(this)

        refreshCartUI()

        findViewById<View>(R.id.checkoutButton)?.setOnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        }

        findViewById<View>(R.id.homeButton)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun refreshCartUI() {
        val items = dbHelper.getCartItems()
        val container = findViewById<LinearLayout>(R.id.cartItemsContainer)
        val emptyText = findViewById<View>(R.id.emptyCartText)
        val restaurantHeader = findViewById<TextView>(R.id.restaurantHeader)
        val summarySection = findViewById<View>(R.id.summarySection)

        container?.removeAllViews()

        if (items.isNotEmpty()) {
            emptyText?.visibility = View.GONE
            restaurantHeader?.visibility = View.VISIBLE
            summarySection?.visibility = View.VISIBLE

            val inflater = LayoutInflater.from(this)
            
            items.forEach { item ->
                val itemView = inflater.inflate(R.layout.item_cart, container, false)
                
                itemView.findViewById<TextView>(R.id.itemName).text = item.foodName
                itemView.findViewById<TextView>(R.id.itemPrice).text = String.format(Locale.US, "%.2f ₽", item.price.toDoubleOrNull() ?: 0.0)
                itemView.findViewById<TextView>(R.id.itemWeight).text = "· ${item.weight}"
                itemView.findViewById<TextView>(R.id.itemQty).text = item.quantity.toString()

                itemView.findViewById<ImageView>(R.id.itemImage).setImageResource(item.imageRes)

                itemView.findViewById<View>(R.id.btnPlus).setOnClickListener {
                    dbHelper.updateCartItemQuantity(item.id, item.quantity + 1)
                    refreshCartUI()
                }

                itemView.findViewById<View>(R.id.btnMinus).setOnClickListener {
                    if (item.quantity > 1) {
                        dbHelper.updateCartItemQuantity(item.id, item.quantity - 1)
                    } else {
                        dbHelper.removeCartItem(item.id)
                    }
                    refreshCartUI()
                }

                container?.addView(itemView)
            }
        } else {
            emptyText?.visibility = View.VISIBLE
            restaurantHeader?.visibility = View.GONE
            summarySection?.visibility = View.GONE
        }
        
        updateTotalSum(items, restaurantHeader)
    }

    private fun updateTotalSum(items: List<com.example.kolobanovda_ki23_16_1b_pr2.data.CartItem>, header: TextView?) {
        var subtotal = 0.0
        items.forEach { 
            val priceVal = it.price.toDoubleOrNull() ?: 0.0
            subtotal += priceVal * it.quantity 
        }
        
        val deliveryFee = 300.0
        val total = subtotal + deliveryFee
        
        findViewById<TextView>(R.id.totalOrderPrice)?.text = String.format(Locale.US, "Сумма заказа %.2f ₽", subtotal)
        findViewById<TextView>(R.id.finalPrice)?.text = String.format(Locale.US, "%.2f ₽", total)
        
        if (items.isNotEmpty()) {
            header?.text = String.format(Locale.US, "Мистер Бургер\n%.2f ₽ · 30-35 мин", subtotal)
        }
    }
}
