package org.comon.moviefriends.presenter.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.presenter.MainActivity
import java.time.LocalTime

const val CHANNEL_ID = "FCM_CHANNEL_ID"
const val CHANNEL_NAME = "PUSH_SESAC"
const val TAG = "FCM_TAG"

class FCMService: FirebaseMessagingService() {

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

    private fun sendToStatusBarPushMessage(title: String, body: String) {

        val intent = Intent(this@FCMService, MainActivity::class.java).apply {
            putExtra("fromFCMRoute", WATCH_TOGETHER_MENU.RECEIVE_LIST.route)
        }

        val pendingIntent = PendingIntent.getActivity(
            this@FCMService,
            LocalTime.now().hashCode(), // requestCode가 고정값이면 나중에 putExtra 값이 null로 나오니 주의...
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo)
                .setContentText(body)
                .setContentIntent(pendingIntent) // 작성한 pendingIntent를 수행
                .setAutoCancel(true) // 알림을 클릭하면 자동으로 상단바에서 지워짐
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        nm.createNotificationChannel(channel)
        nm.notify(notificationID, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        MFPreferences.setFcmToken(token)
    }

}