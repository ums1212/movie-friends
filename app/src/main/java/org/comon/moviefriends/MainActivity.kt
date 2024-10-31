package org.comon.moviefriends

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.comon.moviefriends.ui.screen.HomeScreen
import org.comon.moviefriends.ui.screen.LoginScreen
import org.comon.moviefriends.ui.screen.MovieDetailScreen
import org.comon.moviefriends.ui.screen.NAV_ROUTE
import org.comon.moviefriends.ui.screen.SubmitNickNameScreen
import org.comon.moviefriends.ui.theme.MovieFriendsTheme
import org.comon.moviefriends.ui.viewmodel.LoginViewModel
import org.comon.moviefriends.ui.widget.MFNavigationBar
import org.comon.moviefriends.ui.widget.MFTopAppBar

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

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { MFTopAppBar() },
                bottomBar = { MFNavigationBar() },
            ) { innerPadding ->

                val navController = rememberNavController()
                val startDestination = if (loginViewModel.checkLogin()) NAV_ROUTE.HOME.route else NAV_ROUTE.LOGIN.route

                NavHost(navController = navController, startDestination = startDestination, modifier = Modifier.padding(innerPadding)) {
                    composable(NAV_ROUTE.LOGIN.route) {
                        LoginScreen(navController)
                    }
                    composable(NAV_ROUTE.SUBMIT_NICKNAME.route) {
                        SubmitNickNameScreen(navController)
                    }
                    composable(NAV_ROUTE.HOME.route) {
                        HomeScreen(navController)
                    }
                    composable("${NAV_ROUTE.MOVIE_DETAIL.route}/{movieId}") { backStackEntry ->
                        MovieDetailScreen(movieId = backStackEntry.arguments?.getInt("movieId") ?: 0)
                    }
                }
            }

        }
    }


}