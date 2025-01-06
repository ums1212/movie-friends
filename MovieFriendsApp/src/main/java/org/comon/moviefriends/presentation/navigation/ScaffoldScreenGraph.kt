package org.comon.moviefriends.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sendbird.uikit.compose.navigation.navigateToChannel
import org.comon.moviefriends.common.FullScreenNavRoute
import org.comon.moviefriends.common.IntroNavRoute
import org.comon.moviefriends.common.ScaffoldNavRoute
import org.comon.moviefriends.presentation.screen.community.ChatRoomListScreen
import org.comon.moviefriends.presentation.screen.community.CommunityScreen
import org.comon.moviefriends.presentation.screen.community.ReceiveListScreen
import org.comon.moviefriends.presentation.screen.community.RecommendScreen
import org.comon.moviefriends.presentation.screen.community.RequestListScreen
import org.comon.moviefriends.presentation.screen.community.WatchTogetherScreen
import org.comon.moviefriends.presentation.screen.community.WorldCupScreen
import org.comon.moviefriends.presentation.screen.home.HomeScreen
import org.comon.moviefriends.presentation.screen.profile.ProfileScreen

fun NavGraphBuilder.scaffoldScreenGraph(
    navController: NavHostController,
    closeCommunityTabMenu: () -> Unit
){
    /** 홈 화면 */
    composable(
        route = ScaffoldNavRoute.Home.route,
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) {
        HomeScreen { movieId ->
            navController.navigate("${FullScreenNavRoute.MovieDetail.route}/${movieId}")
        }
    }

    /** 커뮤니티 화면 */
    composable(
        route = ScaffoldNavRoute.Community.route,
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) {
        CommunityScreen(
            moveToCommunityDetailScreen = { postId ->
                navController.navigate("${FullScreenNavRoute.CommunityDetail.route}/${postId}")
            },
            moveToWritePostScreen = {
                closeCommunityTabMenu()
                navController.navigate(FullScreenNavRoute.WritePost.route)
            },
            navigateToLogin = { navController.navigate(IntroNavRoute.Login.route) },
        )
    }

    /** 함께보기 화면 */
    composable(
        route = ScaffoldNavRoute.WatchTogether.route,
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) {
        WatchTogetherScreen(
            navigateToRequestList = { navController.navigate(ScaffoldNavRoute.RequestList.route) },
            navigateToReceiveList = { navController.navigate(ScaffoldNavRoute.ReceiveList.route) },
            navigateToChatRoomList = { navController.navigate(ScaffoldNavRoute.ChatList.route) },
            navigateToMovieDetail = { movieId ->
                navController.navigate("${FullScreenNavRoute.MovieDetail.route}/${movieId}")
            },
        )
    }

    /** 요청 내역 화면 */
    composable(
        route = ScaffoldNavRoute.RequestList.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) {
        RequestListScreen(
            navigateToMovieDetail = { movieId -> navController.navigate("${FullScreenNavRoute.MovieDetail.route}/${movieId}")}
        )
    }

    /** 받은 내역 화면 */
    composable(
        route = ScaffoldNavRoute.ReceiveList.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) {
        ReceiveListScreen(
            navigateToMovieDetail = { movieId -> navController.navigate("${FullScreenNavRoute.MovieDetail.route}/${movieId}")}
        )
    }

    /** 커뮤니티 채팅방 리스트 화면 */
    composable(
        route = ScaffoldNavRoute.ChatList.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) {
        ChatRoomListScreen(
            navigateToChannel = { channelUrl ->
                navController.navigateToChannel(channelUrl)
            }
        )
    }

    /** 영화 추천 화면 */
    composable(
        route = ScaffoldNavRoute.MovieRecommend.route,
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) {
        RecommendScreen()
    }

    /** 영화 월드컵 화면 */
    composable(
        route = ScaffoldNavRoute.MovieWorldCup.route,
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) {
        WorldCupScreen()
    }

    /** 내 정보 화면 */
    composable(
        route = "${ScaffoldNavRoute.Profile.route}/{profileType}",
        arguments = listOf(
            navArgument("profileType"){
                type = NavType.StringType
            }
        ),
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) { backStackEntry ->
        ProfileScreen(
            navigateToUserWant = { navController.navigate(FullScreenNavRoute.ProfileWantMovie.route) },
            navigateToUserRate = { navController.navigate(FullScreenNavRoute.ProfileRate.route) },
            navigateToUserReview = { navController.navigate(FullScreenNavRoute.ProfileReview.route) },
            navigateToUserCommunityPost = { navController.navigate(FullScreenNavRoute.ProfileCommunityPost.route) },
            navigateToUserCommunityReply = { navController.navigate(FullScreenNavRoute.ProfileCommunityReply.route) },
            profileType = backStackEntry.arguments?.getString("profileType") ?: "",
        )
    }
}