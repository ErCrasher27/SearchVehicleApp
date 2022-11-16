package com.example.searchvehicleapp.application

import android.app.Application
import com.example.searchvehicleapp.database.VehicleRoomDatabase

class VehicleApplication : Application() {
    // Using by lazy so the database is only created when needed
    // rather than when the application starts
    val database: VehicleRoomDatabase by lazy { VehicleRoomDatabase.getDatabase(this) }
}