package org.comon.moviefriends.common

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.sendbird.uikit.compose.SendbirdUikitCompose
import com.sendbird.uikit.core.data.model.UikitInitParams
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import org.comon.moviefriends.BuildConfig

@HiltAndroidApp
class MovieFriendsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        movieFriendsApplication = this
        KakaoSdk.init(this, BuildConfig.KAKAO_REST_KEY)

        SendbirdUikitCompose.init(
            UikitInitParams(
                appId = BuildConfig.SENDBIRD_APP_ID,
                context = this,
                isForeground = true
            )
        ).launchIn(MainScope())

    }

    companion object {
        private lateinit var movieFriendsApplication: MovieFriendsApplication
        fun getMovieFriendsApplication() = movieFriendsApplication
    }
}