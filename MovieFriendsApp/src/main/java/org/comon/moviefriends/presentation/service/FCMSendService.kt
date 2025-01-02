package org.comon.moviefriends.presentation.service

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.comon.moviefriends.BuildConfig
import org.json.JSONObject
import java.io.IOException

object FCMSendService {

    private const val TAG = "FCMService"
    private const val FCM_ENDPOINT = "https://fcm.googleapis.com/v1/projects/movie-friends-f7552/messages:send"

    private val client = OkHttpClient()

    suspend fun sendNotificationToToken(token: String, title: String, body: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val accessToken = getAccessToken()
                val jsonBody = JSONObject().apply {
                    put("message", JSONObject().apply {
                        put("token", token)
                        put("data", JSONObject().apply {
                            put("title", title)  // 제목을 data 페이로드에 추가
                            put("body", body)    // 내용을 data 페이로드에 추가
                        })
                    })
                }

                val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

                val request = Request.Builder()
                    .url(FCM_ENDPOINT)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer $accessToken") // Access Token 사용
                    .addHeader("Content-Type", "application/json; UTF-8")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }
                    Log.d(TAG, "Notification sent successfully: ${response.body?.string()}")
                    return@withContext true
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending push notification", e)
                return@withContext false
            }
        }
    }

    private fun getAccessToken(): String? {
        return try {
            val jsonContent = """
            {
              "type": "${BuildConfig.SERVICE_ACCOUNT_TYPE}",
              "project_id": "${BuildConfig.PROJECT_ID}",
              "private_key_id": "${BuildConfig.PRIVATE_KEY_ID}",
              "private_key": "${BuildConfig.PRIVATE_KEY}",
              "client_email": "${BuildConfig.CLIENT_EMAIL}",
              "client_id": "${BuildConfig.CLIENT_ID}",
              "auth_uri": "${BuildConfig.AUTH_URI}",
              "token_uri": "${BuildConfig.TOKEN_URI}",
              "auth_provider_x509_cert_url": "${BuildConfig.AUTH_PROVIDER_X509_CERT_URL}",
              "client_x509_cert_url": "${BuildConfig.CLIENT_X509_CERT_URL}"
            }
        """.trimIndent()

            val inputStream = jsonContent.byteInputStream()
            val credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
            credentials.refreshIfExpired() // 토큰이 만료된 경우 새로고침

            credentials.accessToken.tokenValue // 액세스 토큰 반환
        } catch (e: Exception) {
            Log.e(TAG, "Error obtaining access token", e)
            null
        }
    }
}