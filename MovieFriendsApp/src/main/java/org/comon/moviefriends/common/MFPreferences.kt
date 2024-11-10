package org.comon.moviefriends.common

import android.content.Context
import com.google.gson.Gson
import org.comon.moviefriends.data.model.firebase.UserInfo

object MFPreferences {

    fun getUserInfo(context: Context): UserInfo? {
        val getString = context
            .getSharedPreferences("userInfo", Context.MODE_PRIVATE)
            .getString("userInfo", "")
        return if(getString.isNullOrEmpty()){
            null
        }else{
            Gson().fromJson(getString, UserInfo::class.java)
        }
    }

    fun setUserInfo(context: Context, userInfo: UserInfo) {
        val json = Gson().toJson(userInfo)
        context
            .getSharedPreferences("userInfo",Context.MODE_PRIVATE)
            .edit()
            .putString("userInfo", json)
            .apply()
    }
}