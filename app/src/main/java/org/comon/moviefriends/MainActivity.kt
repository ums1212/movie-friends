package org.comon.moviefriends

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.comon.moviefriends.ui.screen.HomeScreen
import org.comon.moviefriends.ui.screen.LoginScreen
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
            MovieFriendsTheme(
                darkTheme = true
            ) {
                if(loginViewModel.checkLogin()){
                    HomeScreen()
                }else{
                    LoginScreen()
                }
            }
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
}