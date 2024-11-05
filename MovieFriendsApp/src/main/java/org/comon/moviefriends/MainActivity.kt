package org.comon.moviefriends

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.comon.moviefriends.ui.screen.intro.LoginScreen
import org.comon.moviefriends.common.NAV_ROUTE
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.ui.screen.community.ChatRoomListScreen
import org.comon.moviefriends.ui.screen.home.MovieDetailScreen
import org.comon.moviefriends.ui.screen.profile.ProfileSettingScreen
import org.comon.moviefriends.ui.screen.community.PostDetailScreen
import org.comon.moviefriends.ui.screen.intro.ScaffoldScreen
import org.comon.moviefriends.ui.screen.intro.SearchScreen
import org.comon.moviefriends.ui.screen.intro.SubmitNickNameScreen
import org.comon.moviefriends.ui.screen.community.WritePostScreen
import org.comon.moviefriends.ui.theme.FriendsBlack
import org.comon.moviefriends.ui.theme.MovieFriendsTheme
import org.comon.moviefriends.ui.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        settingSplashScreenAnimation()
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MovieFriendsApp()
        }
    }

    /** 스플래시 화면 애니메이션 설정 */
    private fun settingSplashScreenAnimation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 200L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        }
    }

    @Composable
    fun MovieFriendsApp(){
        MovieFriendsTheme(
            darkTheme = true
        ) {
            val navController = rememberNavController()
            val selectedBottomMenuItem = remember { mutableIntStateOf(0) }

//            val startDestination = if (loginViewModel.checkLogin()) NAV_ROUTE.SCAFFOLD.route else NAV_ROUTE.LOGIN.route
            val startDestination = NAV_ROUTE.SCAFFOLD.route

            Box(modifier = Modifier.background(FriendsBlack)){
                NavHost(navController = navController, startDestination = startDestination) {
                    composable(NAV_ROUTE.LOGIN.route) {
                        LoginScreen(
                            { navController.navigate(NAV_ROUTE.SCAFFOLD.route) },
                            { navController.navigate(NAV_ROUTE.SUBMIT_NICKNAME.route) }
                        )
                    }
                    composable(NAV_ROUTE.SUBMIT_NICKNAME.route) {
                        SubmitNickNameScreen { navController.navigate(NAV_ROUTE.SCAFFOLD.route) }
                    }
                    composable(NAV_ROUTE.SCAFFOLD.route) {
                        ScaffoldScreen(navController, selectedBottomMenuItem){ bottomMenuindex ->
                            selectedBottomMenuItem.intValue = bottomMenuindex
                        }
                    }
                    composable(NAV_ROUTE.SEARCH.route) {
                        SearchScreen { navController.navigate(NAV_ROUTE.SCAFFOLD.route) }
                    }

                    /** 상세 화면 */

                    /** 상세 화면 */
                    composable(
                        route = "${NAV_ROUTE.MOVIE_DETAIL.route}/{movieId}",
                        arguments = listOf(
                            navArgument("movieId"){
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        MovieDetailScreen(
                            movieId = backStackEntry.arguments?.getInt("movieId") ?: 0,
                            navigatePop = { navController.popBackStack() }
                        )
                    }
                    composable("${NAV_ROUTE.COMMUNITY_DETAIL.route}/{communityId}") { backStackEntry ->
                        PostDetailScreen(
                            communityId = backStackEntry.arguments?.getInt("communityId") ?: 0,
                            navigatePop = { navController.popBackStack() }
                        )
                    }
                    composable(NAV_ROUTE.WRITE_POST.route) {
                        WritePostScreen(
                            navigateToPostDetail = { communityId ->
                                navController.navigate("${NAV_ROUTE.COMMUNITY_DETAIL.route}/${communityId}") },
                            navigatePop = { navController.popBackStack() }
                        )
                    }
                    composable(WATCH_TOGETHER_MENU.CHAT_ROOM_LIST.route) {
                        ChatRoomListScreen()
                    }
                    composable(NAV_ROUTE.PROFILE_SETTING.route) {
                        ProfileSettingScreen()
                    }
                }
            }


        }
    }


}