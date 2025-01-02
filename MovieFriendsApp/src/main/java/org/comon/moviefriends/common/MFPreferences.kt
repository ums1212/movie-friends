package org.comon.moviefriends.common

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import org.comon.moviefriends.data.entity.firebase.UserInfo

object MFPreferences {

    private const val PREFERENCES_NAME = "mfPreferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var mfPreferences: SharedPreferences

    fun init(context: Context) {
        mfPreferences = context.getSharedPreferences(PREFERENCES_NAME, MODE)
    }

    fun getUserInfo(): UserInfo? {
        val getString = mfPreferences
            .getString("userInfo", "")
        return if(getString.isNullOrEmpty()){
            null
        }else{
            Gson().fromJson(getString, UserInfo::class.java)
        }
    }

    fun setUserInfo(userInfo: UserInfo) {
        val json = Gson().toJson(userInfo)
        mfPreferences.edit()
            .putString("userInfo", json)
            .apply()
    }

    fun getFcmToken() = mfPreferences.getString("fcmToken", "")

    fun setFcmToken(token: String) {
        mfPreferences.edit()
            .putString("fcmToken", token)
            .apply()
    }

    fun getNotiPermission() = mfPreferences.getBoolean("notiPermission", false)

    fun setNotiPermission(isPermission: Boolean) {
        mfPreferences.edit()
            .putBoolean("notiPermission", isPermission)
            .apply()
    }

    fun getSendBirdToken() = mfPreferences.getString("sendBirdToken", "")

    fun setSendBirdToken(token: String){
        mfPreferences.edit()
            .putString("sendBirdToken", token)
            .apply()
    }
}