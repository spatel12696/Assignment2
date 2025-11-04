# 🗺️ SpotFinder — Android App

**SpotFinder** is an Android application that integrates a **local SQLite database** with **Google Maps** to help users explore, store, and manage location data within the **Greater Toronto Area (GTA)**.  
It allows users to **search**, **add**, **update**, and **delete** locations, displaying them visually on an interactive map.

---

## 🚀 Features

- 🔍 **Search & View Locations**
  - Type or select a city or neighborhood from the dropdown.
  - Fetches and displays the latitude and longitude on Google Maps with a marker.

- 🗃️ **Database Management (CRUD)**
  - Add new locations with address, latitude, and longitude.
  - Update existing entries or delete them from the database.
  - Auto-suggest feature while typing using stored data.

- 🧭 **Map Integration**
  - Displays stored locations using Google Maps API.
  - Centers the camera on searched coordinates.
  - Shows the exact coordinates in a text view.

- 🌆 **Preloaded Database**
  - Comes pre-seeded with **100 GTA locations** including Oshawa, Ajax, Mississauga, Brampton, and Toronto neighborhoods.

- 🎨 **Modern UI Theme**
  - Elegant **black and gold** color scheme for a premium look.
  - Material Design 3 components for a smooth and responsive layout.

---

## 🧩 Tech Stack

| Component | Technology Used |
|------------|------------------|
| **Language** | Kotlin |
| **Database** | SQLite (via `SQLiteOpenHelper`) |
| **Map Service** | Google Maps API |
| **UI Framework** | Material Design 3 |
| **IDE** | Android Studio Giraffe / Jellyfish |

---

## 🛠️ How It Works

### 1️⃣ Database
- Uses a local SQLite database named `spotfinder.db`.
- Table: `location` with columns:  
  - `id` (Primary Key)  
  - `address` (City or Area Name)  
  - `latitude`  
  - `longitude`
- DatabaseHelper handles:
  - `insertLocation()`
  - `getLocationByAddress()`
  - `updateLocation()`
  - `deleteLocation()`
  - `getAllAddressesLike()` (for auto-suggestions)

### 2️⃣ Map
- Integrated via `SupportMapFragment`.
- Displays user-searched locations using latitude and longitude.
- Automatically centers and zooms into selected markers.

---

## 📲 Screens Overview

| Screen | Description |
|---------|--------------|
| **MainActivity** | Search bar with Google Map and coordinates display. |
| **ManageLocationActivity** | Add, update, delete, or load locations. |
| **DatabaseHelper** | Handles all CRUD operations and seeds initial GTA data. |

---

## ⚙️ Setup Instructions

### Prerequisites
- Android Studio (latest version)
- Google Maps API key (replace in `AndroidManifest.xml`)
- Internet connection for map loading

### Steps to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/SpotFinder.git
   ```
2. Open in Android Studio.
3. Add your **Google Maps API key** inside `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_API_KEY_HERE" />
   ```
4. Build and run the project on an emulator or physical device.

---

## 🧠 Learning Outcome

SpotFinder demonstrates how:
- **Databases (SQLite)** can persist and manage structured data locally.
- **Maps (Google Maps API)** can visually represent that data.
- Combined, they create a seamless, interactive user experience that connects data management with spatial visualization.

---

## 📸 Screenshots


📍 Main Screen — Searching locations on map
<img width="389" height="861" alt="image" src="https://github.com/user-attachments/assets/ddce581f-fbb5-4966-879a-2febe9b3c852" />

⚙️ Manage Screen — Adding or updating entries
<img width="389" height="865" alt="image" src="https://github.com/user-attachments/assets/4af1af21-a069-46e0-9b22-9c8e6040ad26" />

---
