package com.hellodiffa.dailyworker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val notifyId = 101
        val channelId = "101"
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            notifyId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "a")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Work Manager")
                .setContentText("Good morning! have a nice day!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "channelId", NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notifyId, notification.build())



        return Result.success()
    }
}