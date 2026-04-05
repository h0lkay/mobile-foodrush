package com.example.kolobanovda_ki23_16_1b_pr2.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Модели данных
data class Address(val id: Int, val street: String, val city: String, val state: String, val zip: String)
data class CartItem(
    val id: Int,
    val foodName: String,
    val price: String,
    val quantity: Int,
    val weight: String,
    val imageRes: Int
)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "AppDatabase.db"
        private const val DATABASE_VERSION = 3

        private const val TABLE_ADDRESSES = "addresses"
        private const val KEY_ID = "id"
        private const val KEY_STREET = "street"
        private const val KEY_CITY = "city"
        private const val KEY_STATE = "state"
        private const val KEY_ZIP = "zip"

        private const val TABLE_CART = "cart"
        private const val KEY_CART_ID = "id"
        private const val KEY_FOOD_NAME = "food_name"
        private const val KEY_PRICE = "price"
        private const val KEY_QUANTITY = "quantity"
        private const val KEY_WEIGHT = "weight"
        private const val KEY_IMAGE_RES = "image_res"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createAddressTable = ("CREATE TABLE " + TABLE_ADDRESSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_STREET + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_STATE + " TEXT,"
                + KEY_ZIP + " TEXT" + ")")
        db?.execSQL(createAddressTable)

        val createCartTable = ("CREATE TABLE " + TABLE_CART + "("
                + KEY_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_FOOD_NAME + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_QUANTITY + " INTEGER,"
                + KEY_WEIGHT + " TEXT,"
                + KEY_IMAGE_RES + " INTEGER" + ")")
        db?.execSQL(createCartTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ADDRESSES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    // --- CRUD для АДРЕСОВ ---
    fun addAddress(street: String, city: String, state: String, zip: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_STREET, street)
            put(KEY_CITY, city)
            put(KEY_STATE, state)
            put(KEY_ZIP, zip)
        }
        return db.insert(TABLE_ADDRESSES, null, values).also { db.close() }
    }

    fun getAllAddresses(): List<Address> {
        val list = mutableListOf<Address>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ADDRESSES", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(Address(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_STREET)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_CITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ZIP))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun deleteAddress(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_ADDRESSES, "$KEY_ID=?", arrayOf(id.toString())).also { db.close() }
    }

    // --- CRUD для КОРЗИНЫ (С объединением дубликатов) ---
    fun addToCart(name: String, price: String, quantity: Int, weight: String, imageRes: Int): Long {
        val db = this.writableDatabase
        
        // Нормализуем название для поиска (убираем вес, если он в названии)
        val normalizedName = name.split(" ").firstOrNull() ?: name

        // Проверяем, есть ли уже такой товар
        val cursor = db.rawQuery("SELECT $KEY_CART_ID, $KEY_QUANTITY FROM $TABLE_CART WHERE $KEY_FOOD_NAME LIKE ?", arrayOf("$normalizedName%"))
        
        return if (cursor.moveToFirst()) {
            // Товар найден -> UPDATE
            val id = cursor.getInt(0)
            val currentQty = cursor.getInt(1)
            val values = ContentValues().apply {
                put(KEY_QUANTITY, currentQty + quantity)
            }
            db.update(TABLE_CART, values, "$KEY_CART_ID=?", arrayOf(id.toString())).toLong().also {
                cursor.close()
                db.close()
            }
        } else {
            // Товара нет -> INSERT
            cursor.close()
            val values = ContentValues().apply {
                put(KEY_FOOD_NAME, normalizedName) // Сохраняем чистое название
                put(KEY_PRICE, price)
                put(KEY_QUANTITY, quantity)
                put(KEY_WEIGHT, weight)
                put(KEY_IMAGE_RES, imageRes)
            }
            db.insert(TABLE_CART, null, values).also { db.close() }
        }
    }

    fun getCartItems(): List<CartItem> {
        val list = mutableListOf<CartItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CART", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(CartItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CART_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_FOOD_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_WEIGHT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IMAGE_RES))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun updateCartItemQuantity(id: Int, newQuantity: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_QUANTITY, newQuantity)
        }
        return db.update(TABLE_CART, values, "$KEY_CART_ID=?", arrayOf(id.toString())).also { db.close() }
    }

    fun removeCartItem(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CART, "$KEY_CART_ID=?", arrayOf(id.toString())).also { db.close() }
    }

    fun clearCart() {
        val db = this.writableDatabase
        db.delete(TABLE_CART, null, null)
        db.close()
    }
}
