package org.comon.moviefriends.presenter.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.comon.moviefriends.common.FullScreenNavRoute
import org.comon.moviefriends.common.IntroNavRoute
import org.comon.moviefriends.common.ScaffoldNavRoute
import org.comon.moviefriends.presenter.screen.community.CommunityScreen
import org.comon.moviefriends.presenter.screen.community.ReceiveListScreen
import org.comon.moviefriends.presenter.screen.community.RecommendScreen
import org.comon.moviefriends.presenter.screen.community.RequestListScreen
import org.comon.moviefriends.presenter.screen.community.WatchTogetherScreen
import org.comon.moviefriends.presenter.screen.community.WorldCupScreen
import org.comon.moviefriends.presenter.screen.home.HomeScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileScreen

fun NavGraphBuilder.scaffoldScreenGraph(navController: NavHostController){
    /** 홈 화면 */
    composable(ScaffoldNavRoute.Home.route) {
        HomeScreen { movieId ->
            navController.navigate("${FullScreenNavRoute.MovieDetail.route}/${movieId}")
        }
    }

    /** 커뮤니티 화면 */
    composable(ScaffoldNavRoute.Community.route) {
        CommunityScreen(
            moveToCommunityDetailScreen = { postId ->
                navController.navigate("${FullScreenNavRoute.CommunityDetail.route}/${postId}")
            },
            moveToWritePostScreen = { navController.navigate(FullScreenNavRoute.WritePost.route) },
            navigateToLogin = { navController.navigate(IntroNavRoute.Login.route) },
        )
    }

    /** 함께보기 화면 */
    composable(ScaffoldNavRoute.WatchTogether.route) {
        WatchTogetherScreen(
            navigateToRequestList = { navController.navigate(ScaffoldNavRoute.RequestList.route) },
            navigateToReceiveList = { navController.navigate(ScaffoldNavRoute.ReceiveList.route) },
            navigateToChatRoomList = { navController.navigate(FullScreenNavRoute.ChatRoom.route) },
            navigateToMovieDetail = { movieId ->
                navController.navigate("${FullScreenNavRoute.MovieDetail.route}/${movieId}")
            },
        )
    }

    /** 요청 내역 화면 */
    composable(ScaffoldNavRoute.RequestList.route) {
        RequestListScreen(
            navigateToMovieDetail = { movieId -> navController.navigate("${FullScreenNavRoute.MovieDetail.route}/${movieId}")}
        )
    }

    /** 받은 내역 화면 */
    composable(ScaffoldNavRoute.ReceiveList.route) {
        ReceiveListScreen(
            navigateToMovieDetail = { movieId -> navController.navigate("${FullScreenNavRoute.MovieDetail.route}/${movieId}")}
        )
    }

    /** 영화 추천 화면 */
    composable(ScaffoldNavRoute.MovieRecommend.route) {
        RecommendScreen()
    }

    /** 영화 월드컵 화면 */
    composable(ScaffoldNavRoute.MovieWorldCup.route) {
        WorldCupScreen()
    }

    /** 내 정보 화면 */
    composable(
        route = "${ScaffoldNavRoute.Profile.route}/{profileType}",
        arguments = listOf(
            navArgument("profileType"){
                type = NavType.StringType
            }
        )
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