package com.example.searchvehicleapp.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.searchvehicleapp.MainActivity
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.application.VehicleApplication

class MyGarageReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    // Arbitrary id number
    val notificationId = 17

    override fun doWork(): Result {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(applicationContext, 0, intent, 0)

        val model = inputData.getString(nameKey)

        val builder = NotificationCompat.Builder(applicationContext, VehicleApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_directions_car_24)
            .setContentTitle("Check your insurance!")
            .setContentText("Alert about your kilometres of $model")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }

        return Result.success()
    }

    companion object {
        const val nameKey = "MODEL"
    }
}