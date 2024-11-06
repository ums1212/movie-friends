package org.comon.moviefriends.common

import android.content.Context
import com.google.gson.Gson
import org.comon.moviefriends.data.model.UserInfo

object MFPreferences {

    fun getUserInfo(context: Context): UserInfo =
        Gson().fromJson(
            context
                .getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                .getString("userInfo", ""),
            UserInfo::class.java
        )

    fun setUserInfo(context: Context, userInfo: UserInfo) {
        val json = Gson().toJson(userInfo)
        context
            .getSharedPreferences("userInfo",Context.MODE_PRIVATE)
            .edit()
            .putString("userInfo", json)
            .apply()
    }
}