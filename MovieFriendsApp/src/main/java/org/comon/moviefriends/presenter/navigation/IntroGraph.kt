package org.comon.moviefriends.presenter.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.comon.moviefriends.common.IntroNavRoute
import org.comon.moviefriends.common.ScaffoldNavRoute
import org.comon.moviefriends.presenter.screen.intro.LoginScreen
import org.comon.moviefriends.presenter.screen.intro.SubmitNickNameScreen
import org.comon.moviefriends.presenter.viewmodel.JoinType
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavGraphBuilder.introGraph(navController: NavHostController){
    /** 로그인 화면 */
    composable(
        route = IntroNavRoute.Login.route,
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) {
        LoginScreen(
            moveToScaffoldScreen = { navController.navigate(ScaffoldNavRoute.Home.route) },
            moveToSubmitNickNameScreen = { user, joinType ->
                when(joinType){
                    JoinType.KAKAO.str -> {
                        val encodedUrl = URLEncoder.encode(user?.photoUrl.toString(), StandardCharsets.UTF_8.toString())
                        navController.navigate("${IntroNavRoute.SubmitNickName.route}/${user?.uid}/${user?.displayName}/${encodedUrl}/${joinType}")
                    }
                    JoinType.GOOGLE.str -> {
                        val encodedUrl = URLEncoder.encode(user?.photoUrl.toString(), StandardCharsets.UTF_8.toString())
                        navController.navigate("${IntroNavRoute.SubmitNickName.route}/${user?.uid}//${encodedUrl}/${joinType}")
                    }
                }
            }
        )
    }

    /** 닉네임 입력 화면 */
    composable(
        route = "${IntroNavRoute.SubmitNickName.route}/{uid}/{nickname}/{photoUrl}/{joinType}",
        arguments = listOf(
            navArgument("uid") { type = NavType.StringType },
            navArgument("nickname") { type = NavType.StringType },
            navArgument("photoUrl") { type = NavType.StringType },
            navArgument("joinType") { type = NavType.StringType }
        ),
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            ) + fadeOut()
        },
    ) { backStackEntry ->
        SubmitNickNameScreen(
            uid = backStackEntry.arguments?.getString("uid")?:"",
            nickname = backStackEntry.arguments?.getString("nickname")?:"",
            photoUrl = backStackEntry.arguments?.getString("photoUrl")?:"",
            joinType = backStackEntry.arguments?.getString("joinType")?:"",
            moveToScaffoldScreen = {
                navController.navigate(ScaffoldNavRoute.Home.route){
                    popUpTo(IntroNavRoute.SubmitNickName.route){ inclusive = true }
                }
            }
        )
    }
}