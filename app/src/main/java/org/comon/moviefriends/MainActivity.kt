package org.comon.moviefriends

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.comon.moviefriends.ui.screen.LoginScreen
import org.comon.moviefriends.common.NAV_ROUTE
import org.comon.moviefriends.ui.screen.ScaffoldScreen
import org.comon.moviefriends.ui.screen.SearchScreen
import org.comon.moviefriends.ui.screen.SubmitNickNameScreen
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

//            val startDestination = if (loginViewModel.checkLogin()) NAV_ROUTE.SCAFFOLD.route else NAV_ROUTE.LOGIN.route
            val startDestination = NAV_ROUTE.SCAFFOLD.route

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
                    ScaffoldScreen(navController)
                }
                composable(NAV_ROUTE.SEARCH.route) {
                    SearchScreen { navController.navigate(NAV_ROUTE.SCAFFOLD.route) }
                }
            }

        }
    }


}