package com.example.searchvehicleapp.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.database.VehicleRoomDatabase

class VehicleApplication : Application() {
    // Using by lazy so the database is only created when needed
    // rather than when the application starts
    val database: VehicleRoomDatabase by lazy { VehicleRoomDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "mygarage_reminder_id"
    }
}