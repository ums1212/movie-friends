package org.comon.moviefriends.presenter.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sendbird.uikit.compose.channels.group.detail.ChannelScreen
import com.sendbird.uikit.compose.navigation.sendbirdGroupChannelNavGraph
import org.comon.moviefriends.presenter.theme.FriendsBlack

@Composable
fun MovieFriendsNavigation(
    navController: NavHostController,
    startDestination: String,
    innerPadding: PaddingValues,
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding).background(FriendsBlack)
    ){
        introGraph(navController)
        fullScreenGraph(navController)
        scaffoldScreenGraph(navController)
        sendbirdGroupChannelNavGraph(
            navController = navController,
            channelScreen = { channelUrl ->
                ChannelScreen(navController, channelUrl)
            }
        )
    }
}