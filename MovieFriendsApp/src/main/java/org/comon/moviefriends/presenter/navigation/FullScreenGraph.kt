package org.comon.moviefriends.presenter.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.comon.moviefriends.common.FullScreenNavRoute
import org.comon.moviefriends.common.IntroNavRoute
import org.comon.moviefriends.presenter.screen.community.ChatRoomListScreen
import org.comon.moviefriends.presenter.screen.community.PostDetailScreen
import org.comon.moviefriends.presenter.screen.community.WritePostScreen
import org.comon.moviefriends.presenter.screen.home.MovieDetailScreen
import org.comon.moviefriends.presenter.screen.home.SearchScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileCommunityPostScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileCommunityReplyScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileRateScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileReviewScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileSettingScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileWantMovieScreen
import org.comon.moviefriends.presenter.viewmodel.CommunityPostViewModel

fun NavGraphBuilder.fullScreenGraph(
    navController: NavHostController,
    postViewModel: CommunityPostViewModel
){
    /** 검색 화면 */
    composable(FullScreenNavRoute.Search.route) {
        SearchScreen(navigatePop = { navController.popBackStack() })
    }

    /** 영화 상세 정보 화면 */
    composable(
        route = "${FullScreenNavRoute.MovieDetail.route}/{movieId}",
        arguments = listOf(
            navArgument("movieId"){
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        MovieDetailScreen(
            movieId = backStackEntry.arguments?.getInt("movieId") ?: 0,
            navigatePop = { navController.popBackStack() },
            navigateToLogin = { navController.navigate(IntroNavRoute.Login.route) },
        )
    }

    /** 커뮤니티 상세글 화면 */
    composable("${FullScreenNavRoute.CommunityDetail.route}/{postId}") { backStackEntry ->
        PostDetailScreen(
            postId = backStackEntry.arguments?.getString("postId") ?: "",
        )
    }

    /** 커뮤니티 글작성 화면 */
    composable(FullScreenNavRoute.WritePost.route) {
        WritePostScreen(postId = null, viewModel = postViewModel)
    }

    /** 커뮤니티 글수정 화면 */
    composable("${FullScreenNavRoute.WritePost.route}/{postId}") { backStackEntry ->
        val postId = backStackEntry.arguments?.getString("postId") ?: ""
        WritePostScreen(postId = postId, viewModel = postViewModel)
    }

    /** 커뮤니티 채팅방 화면 */
    composable(FullScreenNavRoute.ChatRoom.route) {
        ChatRoomListScreen()
    }

    /** 내 정보 수정 화면 */
    composable(FullScreenNavRoute.ProfileSetting.route) {
        ProfileSettingScreen()
    }

    /** 내 정보 보고 싶은 영화 화면 */
    composable(FullScreenNavRoute.ProfileWantMovie.route) {
        ProfileWantMovieScreen(
            navigateToLogin = { navController.navigate(IntroNavRoute.Login.route) }
        )
    }

    /** 내 정보 남긴 평점 화면 */
    composable(FullScreenNavRoute.ProfileRate.route) {
        ProfileRateScreen()
    }

    /** 내 정보 남긴 리뷰 화면 */
    composable(FullScreenNavRoute.ProfileReview.route) {
        ProfileReviewScreen()
    }

    /** 내 정보 남긴 커뮤니티 글 화면 */
    composable(FullScreenNavRoute.ProfileCommunityPost.route) {
        ProfileCommunityPostScreen{ postId ->
            navController.navigate("${FullScreenNavRoute.CommunityDetail.route}/${postId}")
        }
    }

    /** 내 정보 남긴 커뮤니티 댓글 화면 */
    composable(FullScreenNavRoute.ProfileCommunityReply.route) {
        ProfileCommunityReplyScreen{ postId ->
            navController.navigate("${FullScreenNavRoute.CommunityDetail.route}/${postId}")
        }
    }
}
