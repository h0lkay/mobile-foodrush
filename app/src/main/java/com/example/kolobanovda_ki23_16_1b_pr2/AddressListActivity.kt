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
import com.example.kolobanovda_ki23_16_1b_pr2.data.Address
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class AddressListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var map: MapView
    private var selectedPosition = -1
    private var addressList = listOf<Address>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Configuration.getInstance().load(this, getSharedPreferences("osm", MODE_PRIVATE))
        setContentView(R.layout.activity_address_list)

        dbHelper = DatabaseHelper(this)
        map = findViewById(R.id.mapView)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        map.controller.setZoom(17.0)

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
        map.onResume()
        refreshAddressList()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun refreshAddressList() {
        addressList = dbHelper.getAllAddresses()
        val container = findViewById<LinearLayout>(R.id.addressContainer)
        container?.removeAllViews()

        // Обновление маркеров на карте
        map.overlays.clear()
        addressList.forEach { addr ->
            if (addr.lat != 0.0 && addr.lon != 0.0) {
                val marker = Marker(map)
                marker.position = GeoPoint(addr.lat, addr.lon)
                marker.title = addr.street
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.setOnMarkerClickListener { _, _ ->
                    openEditAddress(addr)
                    true
                }
                map.overlays.add(marker)
            }
        }
        
        if (addressList.isNotEmpty()) {
            val lastAddr = addressList.last()
            if (lastAddr.lat != 0.0) {
                map.controller.setCenter(GeoPoint(lastAddr.lat, lastAddr.lon))
            }
        }
        map.invalidate()

        val inflater = LayoutInflater.from(this)
        val radioButtons = mutableListOf<RadioButton>()

        addressList.forEachIndexed { index, address ->
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
                
                // Фокусировка на маркере при выборе из списка
                if (address.lat != 0.0) {
                    map.controller.setZoom(18.5)
                    map.controller.animateTo(GeoPoint(address.lat, address.lon))

                    map.overlays.forEach { overlay ->
                        if (overlay is Marker && overlay.position.latitude == address.lat) {
                            overlay.showInfoWindow()
                        }
                    }
                }
            }

            itemView.setOnClickListener(clickListener)
            rb.setOnClickListener(clickListener)

            container?.addView(itemView)
        }
    }

    private fun openEditAddress(address: Address) {
        val intent = Intent(this, AddressActivity::class.java).apply {
            putExtra("lat", address.lat)
            putExtra("lon", address.lon)
            putExtra("street", address.street)
            putExtra("city", address.city)
        }
        startActivity(intent)
    }
}
