package org.comon.moviefriends.data.datasource.sendbird

import com.google.gson.GsonBuilder
import org.comon.moviefriends.data.entity.sendbird.CreateSendBirdUserDto
import org.comon.moviefriends.data.entity.sendbird.ResponseSendBirdUserDto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface SendBirdService {

    // SendBird 사용자 생성
    @POST("users")
    suspend fun createSendBirdUser(
        @Body body: CreateSendBirdUserDto
    ): Response<ResponseSendBirdUserDto>

    companion object {
        private var sendBirdService: SendBirdService? = null
        fun getInstance() : SendBirdService {
            if (sendBirdService == null) {
                val gson = GsonBuilder().serializeNulls().create()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_SENDBIRD_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(SendBirdOkHttpClient.getClient())
                    .build()
                sendBirdService = retrofit.create(SendBirdService::class.java)
            }
            return sendBirdService!!
        }
    }

}