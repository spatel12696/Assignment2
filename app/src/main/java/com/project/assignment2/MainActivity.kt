package com.project.assignment2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.assignment2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding  // View binding for UI access
    private lateinit var googleMap: GoogleMap          // Map object to display locations
    private lateinit var dbHelper: DatabaseHelper      // Database helper instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this) // Initialize local SQLite database

        // 🗺️ Load Google Map and set default view to Toronto
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            val toronto = LatLng(43.6532, -79.3832)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 10f))
        }

        // 🔍 Search button click → perform address lookup
        binding.btnSearch.setOnClickListener {
            searchAddress()
        }

        // 💡 Allow pressing Enter key to trigger search
        binding.addressAuto.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                searchAddress()
                true
            } else false
        }

        // ⚙️ Open Manage Location screen for Add/Update/Delete
        binding.btnManage.setOnClickListener {
            val intent = Intent(this, ManageLocationActivity::class.java)
            startActivity(intent)
        }

        // 🧠 Smart search suggestions while typing
        binding.addressAuto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            // When text changes → show dropdown with matching addresses
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val matches = dbHelper.getAllAddressesLike(query)
                val adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    matches
                )
                binding.addressAuto.setAdapter(adapter)
                if (matches.isNotEmpty()) binding.addressAuto.showDropDown()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // 🔎 Search the database for an address and show it on the map
    private fun searchAddress() {
        val address = binding.addressAuto.text.toString().trim()
        if (address.isEmpty()) {
            Toast.makeText(this, "Enter an address", Toast.LENGTH_SHORT).show()
            return
        }

        val location = dbHelper.getLocationByAddress(address)
        if (location != null) {
            // Center map on the found location and show a marker
            val position = LatLng(location.latitude, location.longitude)
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(position).title(location.address))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 11f))
            binding.tvLatLng.text =
                "Lat: ${location.latitude}, Lng: ${location.longitude}"
        } else {
            // If address not found, show a simple warning message
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
        }
    }
}
