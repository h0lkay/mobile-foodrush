package com.example.kolobanovda_ki23_16_1b_pr2

import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kolobanovda_ki23_16_1b_pr2.data.DatabaseHelper
import com.google.android.material.textfield.TextInputEditText
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.util.Locale

class AddressActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var map: MapView
    private var selectedMarker: Marker? = null
    private var selectedPoint: GeoPoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val osmConfig = Configuration.getInstance()
        osmConfig.userAgentValue = packageName
        osmConfig.load(this, getSharedPreferences("osm", MODE_PRIVATE))
        
        setContentView(R.layout.activity_address)

        dbHelper = DatabaseHelper(this)
        map = findViewById(R.id.mapView)
        
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        
        val mapController = map.controller
        mapController.setZoom(16.0)

        // Центр Красноярска
        val krasnoyarsk = GeoPoint(56.0153, 92.8932)

        val existingLat = intent.getDoubleExtra("lat", 0.0)
        val existingLon = intent.getDoubleExtra("lon", 0.0)

        if (existingLat != 0.0 && existingLon != 0.0) {
            val point = GeoPoint(existingLat, existingLon)
            mapController.setCenter(point)
            setMarker(point)
        } else {
            mapController.setCenter(krasnoyarsk)
        }

        val eventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean = false
            override fun longPressHelper(p: GeoPoint?): Boolean {
                p?.let { setMarker(it) }
                return true
            }
        }
        map.overlays.add(MapEventsOverlay(eventsReceiver))

        findViewById<Button>(R.id.confirmOrderButton)?.setOnClickListener {
            val streetAndHouse = findViewById<TextInputEditText>(R.id.streetAddressEditText)?.text.toString().trim()
            val city = findViewById<TextInputEditText>(R.id.cityEditText)?.text.toString().trim()
            val apt = findViewById<TextInputEditText>(R.id.aptEditText)?.text.toString().trim()
            val entrance = findViewById<TextInputEditText>(R.id.entranceEditText)?.text.toString().trim()
            val point = selectedPoint

            if (streetAndHouse.isNotEmpty() && point != null) {
                var fullStreet = streetAndHouse
                if (apt.isNotEmpty()) fullStreet += ", кв. $apt"
                if (entrance.isNotEmpty()) fullStreet += ", под. $entrance"

                dbHelper.addAddress(fullStreet, city, "", "", point.latitude, point.longitude)
                Toast.makeText(this, "Адрес сохранен", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Поставьте метку на карте и укажите адрес", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setMarker(point: GeoPoint) {
        selectedPoint = point
        if (selectedMarker == null) {
            selectedMarker = Marker(map)
            selectedMarker?.isDraggable = true
            map.overlays.add(selectedMarker)
        }
        selectedMarker?.position = point
        selectedMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.invalidate()
        updateAddressFromPoint(point)
    }

    private fun updateAddressFromPoint(point: GeoPoint) {
        val geocoder = Geocoder(this, Locale("ru"))
        try {
            val addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]

                val street = address.thoroughfare ?: ""
                val house = address.subThoroughfare ?: ""
                val fullStreet = if (house.isNotEmpty()) "$street, $house" else street
                
                findViewById<TextInputEditText>(R.id.streetAddressEditText)?.setText(fullStreet)
                findViewById<TextInputEditText>(R.id.cityEditText)?.setText(address.locality ?: "Красноярск")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
