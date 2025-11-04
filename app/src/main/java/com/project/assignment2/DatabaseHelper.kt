package com.project.assignment2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        // Database name and version
        private const val DB_NAME = "spotfinder.db"
        private const val DB_VERSION = 1

        // Table and column names for storing location data
        const val TABLE = "location"
        const val COL_ID = "id"
        const val COL_ADDRESS = "address"
        const val COL_LAT = "latitude"
        const val COL_LNG = "longitude"
    }

    // Called once when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        // SQL command to create a location table with id, address, latitude, and longitude
        val createTable = """
            CREATE TABLE $TABLE (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ADDRESS TEXT UNIQUE,
                $COL_LAT REAL,
                $COL_LNG REAL
            )
        """.trimIndent()

        db.execSQL(createTable) // Execute SQL command
        seedGTA(db) // Populate database with 100 GTA locations initially
    }

    // Called when the database version changes — rebuilds the table if needed
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    // ---------------------------------------------------------------
    //                      CRUD Operations
    // ---------------------------------------------------------------

    // Add a new location (address + coordinates)
    fun insertLocation(address: String, lat: Double, lng: Double): Boolean {
        val cv = ContentValues().apply {
            put(COL_ADDRESS, address.trim().lowercase()) // normalize address
            put(COL_LAT, lat)
            put(COL_LNG, lng)
        }
        // Returns true if insert succeeded, false if it failed (e.g., duplicate address)
        return writableDatabase.insert(TABLE, null, cv) != -1L
    }

    // Fetch a location from the database using its address
    fun getLocationByAddress(address: String): LocationModel? {
        readableDatabase.rawQuery(
            "SELECT $COL_ID, $COL_ADDRESS, $COL_LAT, $COL_LNG FROM $TABLE WHERE LOWER($COL_ADDRESS)=LOWER(?)",
            arrayOf(address.trim())
        ).use { c ->
            if (c.moveToFirst()) {
                // Build and return a LocationModel if found
                return LocationModel(
                    id = c.getLong(0),
                    address = c.getString(1),
                    latitude = c.getDouble(2),
                    longitude = c.getDouble(3)
                )
            }
        }
        return null // Return null if address not found
    }

    // Update existing location coordinates
    fun updateLocation(address: String, newLat: Double, newLng: Double): Boolean {
        val cv = ContentValues().apply {
            put(COL_LAT, newLat)
            put(COL_LNG, newLng)
        }
        // Returns true if a record was updated, false if not found
        return writableDatabase.update(
            TABLE,
            cv,
            "LOWER($COL_ADDRESS)=LOWER(?)",
            arrayOf(address.trim())
        ) > 0
    }

    // Delete a location entry based on address
    fun deleteLocation(address: String): Boolean {
        return writableDatabase.delete(
            TABLE,
            "LOWER($COL_ADDRESS)=LOWER(?)",
            arrayOf(address.trim())
        ) > 0
    }

    // Return up to 10 address suggestions matching user input for auto-complete dropdown
    fun getAllAddressesLike(prefix: String): List<String> {
        val list = mutableListOf<String>()
        if (prefix.isEmpty()) return list
        readableDatabase.rawQuery(
            "SELECT $COL_ADDRESS FROM $TABLE WHERE LOWER($COL_ADDRESS) LIKE LOWER(?) ORDER BY $COL_ADDRESS LIMIT 10",
            arrayOf("$prefix%")
        ).use { c ->
            while (c.moveToNext()) list.add(c.getString(0))
        }
        return list
    }

    // ---------------------------------------------------------------
    //                      GTA Seed Data (100 Locations)
    // ---------------------------------------------------------------

    // Preloads 100 locations from across the Greater Toronto Area (GTA)
    // so that the user can instantly search and view them on the map.
    private fun seedGTA(db: SQLiteDatabase) {
        val locations = listOf(
            Triple("oshawa", 43.8971, -78.8658),
            Triple("whitby", 43.8964, -78.9429),
            Triple("ajax", 43.8509, -79.0204),
            Triple("pickering", 43.8384, -79.0868),
            Triple("scarborough", 43.7731, -79.2578),
            Triple("downtown toronto", 43.6532, -79.3832),
            Triple("north york", 43.7615, -79.4111),
            Triple("etobicoke", 43.6205, -79.5132),
            Triple("mississauga", 43.5890, -79.6441),
            Triple("brampton", 43.7315, -79.7624),
            Triple("vaughan", 43.8372, -79.5083),
            Triple("markham", 43.8561, -79.3370),
            Triple("richmond hill", 43.8828, -79.4403),
            Triple("oakville", 43.4675, -79.6877),
            Triple("milton", 43.5183, -79.8774),
            Triple("burlington", 43.3255, -79.7990),
            Triple("aurora", 44.0065, -79.4504),
            Triple("newmarket", 44.0592, -79.4613),
            Triple("king city", 43.9282, -79.5280),
            Triple("caledon", 43.8575, -79.8580),
            Triple("stouffville", 43.9706, -79.2422),
            Triple("unionville", 43.8616, -79.3104),
            Triple("thornhill", 43.8067, -79.4240),
            Triple("woodbridge", 43.7925, -79.5922),
            Triple("bolton", 43.8748, -79.7351),
            Triple("georgetown", 43.6460, -79.9033),
            Triple("acton", 43.6285, -80.0385),
            Triple("brooklin", 43.9607, -78.9570),
            Triple("uxbridge", 44.1128, -79.1226),
            Triple("port perry", 44.1051, -78.9444),
            Triple("caledonia", 43.0669, -79.9536),
            Triple("hamilton", 43.2557, -79.8711),
            Triple("grimsby", 43.2001, -79.5605),
            Triple("beamsville", 43.1642, -79.4772),
            Triple("niagara falls", 43.0896, -79.0849),
            Triple("st. catharines", 43.1594, -79.2469),
            Triple("welland", 42.9920, -79.2483),
            Triple("port credit", 43.5519, -79.5873),
            Triple("clarkson", 43.5082, -79.6382),
            Triple("erin mills", 43.5475, -79.6803),
            Triple("streetsville", 43.5803, -79.7162),
            Triple("meadowvale", 43.5996, -79.7509),
            Triple("malton", 43.7111, -79.6370),
            Triple("cooksville", 43.5789, -79.6178),
            Triple("port union", 43.7853, -79.1312),
            Triple("morningside", 43.7739, -79.1994),
            Triple("guildwood", 43.7440, -79.1980),
            Triple("kennedy park", 43.7167, -79.2654),
            Triple("the beaches", 43.6712, -79.2961),
            Triple("leslieville", 43.6661, -79.3325),
            Triple("riverside", 43.6583, -79.3512),
            Triple("distillery district", 43.6506, -79.3596),
            Triple("liberty village", 43.6386, -79.4222),
            Triple("high park", 43.6465, -79.4637),
            Triple("roncesvalles", 43.6435, -79.4493),
            Triple("the junction", 43.6679, -79.4718),
            Triple("bloordale", 43.6578, -79.4385),
            Triple("bloor west village", 43.6502, -79.4869),
            Triple("forest hill", 43.6934, -79.4206),
            Triple("yorkville", 43.6718, -79.3933),
            Triple("kensington market", 43.6547, -79.4004),
            Triple("queen west", 43.6476, -79.3968),
            Triple("trinity bellwoods", 43.6470, -79.4131),
            Triple("dufferin grove", 43.6595, -79.4323),
            Triple("eglinton west", 43.6993, -79.4370),
            Triple("midtown", 43.7045, -79.3975),
            Triple("lawrence park", 43.7342, -79.4021),
            Triple("york mills", 43.7489, -79.3868),
            Triple("don mills", 43.7355, -79.3431),
            Triple("bayview village", 43.7712, -79.3763),
            Triple("willowdale", 43.7746, -79.4148),
            Triple("flemingdon park", 43.7094, -79.3353),
            Triple("leaside", 43.7073, -79.3679),
            Triple("east york", 43.6896, -79.3300),
            Triple("weston", 43.7009, -79.5144),
            Triple("mount dennis", 43.6872, -79.4879),
            Triple("keelesdale", 43.6825, -79.4706),
            Triple("rockcliffe smyth", 43.6744, -79.4813),
            Triple("junction triangle", 43.6586, -79.4439),
            Triple("seaton village", 43.6663, -79.4149),
            Triple("annex", 43.6683, -79.4057),
            Triple("university", 43.6629, -79.3957),
            Triple("harbourfront", 43.6396, -79.3825),
            Triple("cityplace", 43.6414, -79.3910),
            Triple("fort york", 43.6375, -79.4048),
            Triple("parkdale", 43.6406, -79.4300),
            Triple("swansea", 43.6478, -79.4725),
            Triple("mimico", 43.6191, -79.4911),
            Triple("long branch", 43.5905, -79.5419),
            Triple("new toronto", 43.5985, -79.5155),
            Triple("cliffside", 43.7113, -79.2431),
            Triple("birch cliff", 43.6917, -79.2692),
            Triple("guildwood village", 43.7453, -79.1879),
            Triple("rouge", 43.8054, -79.1565),
            Triple("malvern", 43.8080, -79.2232),
            Triple("centennial", 43.7856, -79.1392),
            Triple("west hill", 43.7732, -79.1867),
            Triple("woburn", 43.7599, -79.2273),
            Triple("bendale", 43.7578, -79.2550),
            Triple("ionview", 43.7273, -79.2657),
            Triple("kennedy commons", 43.7728, -79.2875),
            Triple("agincourt", 43.7857, -79.2743),
            Triple("milliken", 43.8256, -79.2876),
            Triple("steeles", 43.8238, -79.3324),
            Triple("armour heights", 43.7554, -79.4157),
            Triple("lawrence heights", 43.7155, -79.4406),
            Triple("york university heights", 43.7639, -79.4951),
            Triple("downsview", 43.7342, -79.4904)
        )

        // Insert all locations into the table
        for ((address, lat, lng) in locations) {
            val cv = ContentValues().apply {
                put(COL_ADDRESS, address)
                put(COL_LAT, lat)
                put(COL_LNG, lng)
            }
            db.insert(TABLE, null, cv)
        }

        // Log a success message confirming that 100 records were added
        Log.i("DatabaseHelper", "✅ Seeded ${locations.size} GTA locations.")
    }
}
