package com.project.assignment2

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.assignment2.databinding.ActivityManageLocationBinding

class ManageLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageLocationBinding   // View binding for UI elements
    private lateinit var dbHelper: DatabaseHelper                  // Database helper for CRUD operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // 🔙 Handles the back arrow in the toolbar
        // When pressed, closes this screen and returns smoothly to MainActivity
        binding.topAppBar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        // ➕ ADD new location to the database
        binding.btnAdd.setOnClickListener {
            val address = binding.inputAddress.text?.toString()?.trim()?.lowercase()
            val latText = binding.inputLat.text?.toString()?.trim()
            val lngText = binding.inputLng.text?.toString()?.trim()

            // Validate all fields before inserting
            if (address.isNullOrEmpty() || latText.isNullOrEmpty() || lngText.isNullOrEmpty()) {
                showToast("⚠️ Please fill all fields.")
                return@setOnClickListener
            }

            // Convert text to numeric values
            val lat = latText.toDoubleOrNull()
            val lng = lngText.toDoubleOrNull()

            // Check for invalid coordinates
            if (lat == null || lng == null) {
                showToast("❌ Invalid latitude or longitude.")
                return@setOnClickListener
            }

            // Insert location into database and show feedback
            val added = dbHelper.insertLocation(address, lat, lng)
            if (added) {
                showToast("✅ Location added successfully!")
                clearFields()
            } else {
                showToast("⚠️ Address already exists.")
            }
        }

        // ✏️ UPDATE an existing location’s coordinates
        binding.btnUpdate.setOnClickListener {
            val address = binding.inputAddress.text?.toString()?.trim()?.lowercase()
            val latText = binding.inputLat.text?.toString()?.trim()
            val lngText = binding.inputLng.text?.toString()?.trim()

            // Validate input fields
            if (address.isNullOrEmpty() || latText.isNullOrEmpty() || lngText.isNullOrEmpty()) {
                showToast("⚠️ Please fill all fields.")
                return@setOnClickListener
            }

            val lat = latText.toDoubleOrNull()
            val lng = lngText.toDoubleOrNull()

            // Validate coordinate values
            if (lat == null || lng == null) {
                showToast("❌ Invalid coordinates.")
                return@setOnClickListener
            }

            // Update record in database
            val updated = dbHelper.updateLocation(address, lat, lng)
            if (updated) {
                showToast("✅ Location updated!")
                clearFields()
            } else {
                showToast("⚠️ Address not found.")
            }
        }

        // 🗑️ DELETE a saved location
        binding.btnDelete.setOnClickListener {
            val address = binding.inputAddress.text?.toString()?.trim()?.lowercase()
            if (address.isNullOrEmpty()) {
                showToast("⚠️ Enter address to delete.")
                return@setOnClickListener
            }

            // Delete record from database
            val deleted = dbHelper.deleteLocation(address)
            if (deleted) {
                showToast("🗑️ Deleted successfully!")
                clearFields()
            } else {
                showToast("⚠️ Address not found.")
            }
        }

        // 🔍 LOAD existing location data by address
        binding.btnLoad.setOnClickListener {
            val address = binding.inputAddress.text?.toString()?.trim()?.lowercase()
            if (address.isNullOrEmpty()) {
                showToast("⚠️ Enter address to load.")
                return@setOnClickListener
            }

            // Fetch the location details and display them in input fields
            val location = dbHelper.getLocationByAddress(address)
            if (location != null) {
                binding.inputLat.setText(location.latitude.toString())
                binding.inputLng.setText(location.longitude.toString())
                showToast("📍 Loaded ${location.address}")
            } else {
                showToast("⚠️ Address not found.")
            }
        }
    }

    // ✨ Helper: Custom gold-styled Toast messages for consistent UI theme
    private fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        val view = toast.view
        view?.setBackgroundColor(Color.parseColor("#1E1E1E")) // Black background
        val text = view?.findViewById(android.R.id.message) as? android.widget.TextView
        text?.setTextColor(Color.parseColor("#FFD700")) // Gold text
        toast.show()
    }

    // ✨ Helper: Clear all input fields after an operation
    private fun clearFields() {
        binding.inputAddress.text?.clear()
        binding.inputLat.text?.clear()
        binding.inputLng.text?.clear()
    }
}
