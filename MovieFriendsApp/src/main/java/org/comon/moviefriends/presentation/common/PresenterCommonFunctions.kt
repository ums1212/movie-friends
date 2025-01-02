package org.comon.moviefriends.presentation.common

import com.sendbird.uikit.compose.navigation.SendbirdNavigationRoute
import org.comon.moviefriends.common.FullScreenNavRoute
import org.comon.moviefriends.common.IntroNavRoute
import org.comon.moviefriends.common.ScaffoldNavRoute
import org.comon.moviefriends.data.entity.firebase.UserInfo

fun checkTopBarNeedTitle(route: String) =
    !FullScreenNavRoute::class.sealedSubclasses.map { it.objectInstance?.route }.contains(route)
            && !route.contains(FullScreenNavRoute.MovieDetail.route)
            && !route.contains(FullScreenNavRoute.CommunityDetail.route)
            && route != ScaffoldNavRoute.RequestList.route
            && route != ScaffoldNavRoute.ReceiveList.route
            && route != ScaffoldNavRoute.ChatList.route

fun checkTopBarNeedNavigationIcon(route: String) =
    listOf(
        FullScreenNavRoute.CommunityDetail.route,
        FullScreenNavRoute.WritePost.route,
        ScaffoldNavRoute.RequestList.route,
        ScaffoldNavRoute.ReceiveList.route,
        ScaffoldNavRoute.ChatList.route,
        FullScreenNavRoute.ProfileWantMovie.route,
        FullScreenNavRoute.ProfileRate.route,
        FullScreenNavRoute.ProfileReview.route,
        FullScreenNavRoute.ProfileCommunityPost.route,
        FullScreenNavRoute.ProfileCommunityReply.route,
    ).contains(route)
            || route.contains(FullScreenNavRoute.MovieDetail.route)
            || route.contains(FullScreenNavRoute.CommunityDetail.route)

fun checkTopBarNeedCommunityMenuButton(route: String) =
    route == FullScreenNavRoute.CommunityDetail.route ||
            listOf(
                ScaffoldNavRoute.Community.route,
                ScaffoldNavRoute.WatchTogether.route,
                ScaffoldNavRoute.MovieRecommend.route,
                ScaffoldNavRoute.MovieWorldCup.route,
            ).contains(route)

fun checkTopBarNeedSearchButton(route: String) =
    listOf(
        ScaffoldNavRoute.Home.route,
        FullScreenNavRoute.MovieDetail.route,
        ScaffoldNavRoute.Community.route,
        FullScreenNavRoute.CommunityDetail.route,
        ScaffoldNavRoute.WatchTogether.route,
        ScaffoldNavRoute.MovieRecommend.route,
        ScaffoldNavRoute.MovieWorldCup.route,
    ).contains(route)


fun checkCommunityTabItemNeedLogin(route: String, user: UserInfo?) =
    listOf(
        ScaffoldNavRoute.WatchTogether.route,
        ScaffoldNavRoute.MovieRecommend.route
    ).contains(route) && (user == null)

fun checkScreenNeedTopBar(route: String?) =
    route != FullScreenNavRoute.Search.route
            && !IntroNavRoute::class.sealedSubclasses.map { it.objectInstance?.route }.contains(route)
            && route?.contains(SendbirdNavigationRoute.Channel.Route) != true
            && route?.contains("sb_route_channel") == false

fun checkScreenNeedBottomBar(route: String?) =
    route in ScaffoldNavRoute::class.sealedSubclasses.map { it.objectInstance?.route }
            || route?.contains(ScaffoldNavRoute.Profile.route) == true