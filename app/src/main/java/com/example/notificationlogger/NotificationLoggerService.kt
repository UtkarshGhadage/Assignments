package com.example.notificationlogger

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.notificationlogger.Models.LogDataModel
import com.google.firebase.firestore.FirebaseFirestore

class NotificationLoggerService : NotificationListenerService() {

    private val TAG = "NotificationLogger"
    private val CHANNEL_ID = "notification_logger_channel"

    private val db = FirestoreManager.checkAndGetInstance()


    override fun onCreate() {
        super.onCreate()
        Log.d("INVOCATION", "TempOncreateInitialize")

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

    val processedNotifications = HashSet<String>()

    override fun onNotificationPosted(sbn: StatusBarNotification) {
     //Log notification details as before
        super.onNotificationPosted(sbn)

        var key = sbn.key

        if(!processedNotifications.contains(key)) {
            processedNotifications.add(key)

            Log.d("INVOCATION", "Notification Posted Service Invoked")
            val mNotification = sbn.notification
            val extras = mNotification.extras
            val title = extras.getString(Notification.EXTRA_TITLE)
            val message = extras.getCharSequence(Notification.EXTRA_TEXT)
            val appName = sbn.packageName
            val timestamp = System.currentTimeMillis()


            Log.d(TAG, "**Notification posted**")
            Log.d(TAG, "- Title: $title")
            Log.d(TAG, "- Message: $message")
            Log.d(TAG, "- Package: $appName")
            Log.d(TAG, "- Timestamp: $timestamp")

            var newData =
                LogDataModel(timestamp, title, message.toString(), appName, timestamp.toString())

            db.collection("LogTable").add(newData).addOnSuccessListener {
                Log.d(TAG, "Notification saved successfully to Firestore!")
            }
                .addOnFailureListener {
                    Log.e(TAG, "Error saving notification to Firestore: $it")
                }
        }

    }


}

object FirestoreManager {

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getFirestore(): FirebaseFirestore {
        return db
    }

    private fun isInitialized(): Boolean {
        return db != null
    }

    fun checkAndGetInstance(): FirebaseFirestore {
        if (!isInitialized()) {
            throw FirestoreNotInitializedException()
        }
        return getFirestore()
    }

    class FirestoreNotInitializedException : RuntimeException("Firestore instance not initialized")

}
