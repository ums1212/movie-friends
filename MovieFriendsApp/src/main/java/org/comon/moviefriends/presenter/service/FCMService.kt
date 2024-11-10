package org.comon.moviefriends.presenter.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences

const val CHANNEL_ID = "FCM_CHANNEL_ID"
const val CHANNEL_NAME = "PUSH_SESAC"
const val TAG = "FCM_TAG"

class FCMService: FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(pushMessage: RemoteMessage) {
        super.onMessageReceived(pushMessage)

        val pushTitle = pushMessage.data["title"]
        val pushBody = pushMessage.data["body"]
        if( pushTitle != null && pushBody != null){
            sendToStatusBarPushMessage(pushTitle, pushBody)
        }else{
            Log.e(TAG, "FCM 이 Null 로 왔네요")
        }
    }

    private var notificationID = 9312

    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendToStatusBarPushMessage(title: String, body: String) {
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo)
                .setContentText(body)
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            nm.createNotificationChannel(channel)
        }
        nm.notify(notificationID, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        MFPreferences.setFcmToken(token)
    }

}