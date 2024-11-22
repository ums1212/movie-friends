package org.comon.moviefriends.presenter.screen.community

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sendbird.uikit.compose.SendbirdUikitCompose
import com.sendbird.uikit.compose.navigation.SendbirdNavigation
import com.sendbird.uikit.compose.navigation.sendbirdGroupChannelNavGraph
import com.sendbird.uikit.core.data.model.UikitCurrentUserInfo
import org.comon.moviefriends.common.MFPreferences

const val CHANNEL_URL = "YOUR CHANNEL_URL"

@Composable
fun ChatRoomListScreen() {

    val user = MFPreferences.getUserInfo()
    // Prepare user information.
    LaunchedEffect(Unit) {
        SendbirdUikitCompose.prepare(
            UikitCurrentUserInfo(
                userId = user?.sendBirdId ?: "",
                authToken = user?.sendBirdToken ?: ""
            )
        )
    }

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SendbirdNavigation.GroupChannel.route
    ) {
        sendbirdGroupChannelNavGraph(
            navController = navController,
        )
    }
}