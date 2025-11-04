plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.project.assignment2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.project.assignment2"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // ✅ buildConfigField must be inside defaultConfig, not buildFeatures
        buildConfigField("String", "DB_NAME", "\"spotfinder.db\"")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:maps-ktx:5.1.1")
    implementation("com.google.maps.android:maps-utils-ktx:5.1.1")
}
