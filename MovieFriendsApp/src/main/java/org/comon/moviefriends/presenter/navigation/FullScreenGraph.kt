package org.comon.moviefriends.presenter.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.comon.moviefriends.common.FullScreenNavRoute
import org.comon.moviefriends.common.IntroNavRoute
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

fun NavGraphBuilder.fullScreenGraph(
    navController: NavHostController,
){
    /** 검색 화면 */
    composable(
        route = FullScreenNavRoute.Search.route,
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
        SearchScreen(navigatePop = { navController.popBackStack() })
    }

    /** 영화 상세 정보 화면 */
    composable(
        route = "${FullScreenNavRoute.MovieDetail.route}/{movieId}",
        arguments = listOf(
            navArgument("movieId"){
                type = NavType.IntType
            }
        ),
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
    ) { backStackEntry ->
        MovieDetailScreen(
            movieId = backStackEntry.arguments?.getInt("movieId") ?: 0,
            navigatePop = { navController.popBackStack() },
            navigateToLogin = { navController.navigate(IntroNavRoute.Login.route) },
        )
    }

    /** 커뮤니티 상세글 화면 */
    composable(
        route = "${FullScreenNavRoute.CommunityDetail.route}/{postId}",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) { backStackEntry ->
        PostDetailScreen(
            postId = backStackEntry.arguments?.getString("postId") ?: "",
        )
    }

    /** 커뮤니티 글작성 화면 */
    composable(
        route = FullScreenNavRoute.WritePost.route,
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
        WritePostScreen(
            postId = null,
            navigateToPostDetail = { postId ->
                navController.navigate("${FullScreenNavRoute.CommunityDetail.route}/${postId}")
            }
        )
    }

    /** 커뮤니티 글수정 화면 */
    composable(
        route = "${FullScreenNavRoute.WritePost.route}/{postId}",
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
    ) { backStackEntry ->
        val postId = backStackEntry.arguments?.getString("postId") ?: ""
        WritePostScreen(
            postId = postId,
            navigateToPostDetail = { _ ->
                navController.navigate("${FullScreenNavRoute.CommunityDetail.route}/${postId}")
            }
        )
    }

    /** 내 정보 수정 화면 */
    composable(
        route = FullScreenNavRoute.ProfileSetting.route,
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
        ProfileSettingScreen()
    }

    /** 내 정보 보고 싶은 영화 화면 */
    composable(
        route = FullScreenNavRoute.ProfileWantMovie.route,
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
        ProfileWantMovieScreen(
            navigateToLogin = { navController.navigate(IntroNavRoute.Login.route) }
        )
    }

    /** 내 정보 남긴 평점 화면 */
    composable(
        route = FullScreenNavRoute.ProfileRate.route,
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
        ProfileRateScreen()
    }

    /** 내 정보 남긴 리뷰 화면 */
    composable(
        route = FullScreenNavRoute.ProfileReview.route,
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
        ProfileReviewScreen()
    }

    /** 내 정보 남긴 커뮤니티 글 화면 */
    composable(
        route = FullScreenNavRoute.ProfileCommunityPost.route,
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
        ProfileCommunityPostScreen{ postId ->
            navController.navigate("${FullScreenNavRoute.CommunityDetail.route}/${postId}")
        }
    }

    /** 내 정보 남긴 커뮤니티 댓글 화면 */
    composable(
        route = FullScreenNavRoute.ProfileCommunityReply.route,
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
        ProfileCommunityReplyScreen{ postId ->
            navController.navigate("${FullScreenNavRoute.CommunityDetail.route}/${postId}")
        }
    }
}