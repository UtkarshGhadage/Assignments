package com.example.notificationlogger

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationLoggerService : NotificationListenerService() {

    private val TAG = "NotificationLogger"
    private val CHANNEL_ID = "notification_logger_channel"
    private val CHANNEL_NAME = "Channel1"

    override fun onCreate() {
        super.onCreate()
        Log.d("INVOCATION", "TempOncreateInitialize")

        createNotificationChannel()
        startForegroundService()
    }

    private fun startForegroundService() {

        Log.d("INVOCATION", "Foreground Service Invoked")
        val notificationIntent = Intent(this, NotificationLoggerService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification Logger")
            .setContentText("Continuously logging notifications")
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
     //Log notification details as before
        super.onNotificationPosted(sbn)
        Log.d("INVOCATION", "Notification Posted Service Invoked")
        val mNotification = sbn.notification
        val extras = mNotification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)
        val message = extras.getCharSequence(Notification.EXTRA_TEXT)
        val timestamp = System.currentTimeMillis()


        Log.d(TAG, "**Notification posted**")
        Log.d(TAG, "- Title: $title")
        Log.d(TAG, "- Message: $message")
        Log.d(TAG, "- Package: $packageName")
        Log.d(TAG, "- Timestamp: $timestamp")

    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
        channel.description = "Notification logging service channel"
        notificationManager.createNotificationChannel(channel)
    }
}
