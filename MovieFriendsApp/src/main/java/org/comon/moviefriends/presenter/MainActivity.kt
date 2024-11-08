package org.comon.moviefriends.presenter

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kakao.sdk.common.KakaoSdk
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.BuildConfig
import org.comon.moviefriends.presenter.screen.intro.LoginScreen
import org.comon.moviefriends.common.NAV_ROUTE
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.presenter.screen.community.ChatRoomListScreen
import org.comon.moviefriends.presenter.screen.home.MovieDetailScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileSettingScreen
import org.comon.moviefriends.presenter.screen.community.PostDetailScreen
import org.comon.moviefriends.presenter.screen.intro.ScaffoldScreen
import org.comon.moviefriends.presenter.screen.intro.SearchScreen
import org.comon.moviefriends.presenter.screen.intro.SubmitNickNameScreen
import org.comon.moviefriends.presenter.screen.community.WritePostScreen
import org.comon.moviefriends.presenter.theme.FriendsBlack
import org.comon.moviefriends.presenter.theme.MovieFriendsTheme
import org.comon.moviefriends.presenter.viewmodel.LoginResult
import org.comon.moviefriends.presenter.viewmodel.LoginViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    private val loginViewModel:LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        settingSplashScreenAnimation()
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        checkLogin(startDestination)
        splashScreen.setKeepOnScreenCondition{ false }
        setContent {
            MovieFriendsApp(startDestination)
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

    private val startDestination = mutableStateOf(NAV_ROUTE.LOGIN.route)

    private fun checkLogin(startDestination: MutableState<String>){
        lifecycleScope.launch {
            loginViewModel.checkLogin().collectLatest { result ->
                when(result){
                    is LoginResult.Loading -> Log.d("checkLogin", "로그인여부확인로딩")
                    is LoginResult.Success -> {
                        Log.d("checkLogin", "로그인여부확인")
                        if(result.resultData) {
                            startDestination.value = NAV_ROUTE.SCAFFOLD.route
                        }else{
                            startDestination.value = NAV_ROUTE.LOGIN.route
                        }
                    }
                    else -> {
                        Log.d("checkLogin", "로그인여부실패")
                        startDestination.value = NAV_ROUTE.LOGIN.route
                    }
                }
            }
        }
    }

    @Composable
    fun MovieFriendsApp(
        startDestination: MutableState<String>
    ){
        MovieFriendsTheme(
            darkTheme = true
        ) {
            val navController = rememberNavController()
            val selectedBottomMenuItem = remember { mutableIntStateOf(0) }

            Box(modifier = Modifier
                .background(FriendsBlack)
                .fillMaxSize()){
                NavHost(navController = navController, startDestination = startDestination.value) {
                    /** 로그인 화면 */
                    composable(NAV_ROUTE.LOGIN.route) {
                        LoginScreen(
                            { navController.navigate(NAV_ROUTE.SCAFFOLD.route) },
                            { user, joinType ->
                                val encodedUrl = URLEncoder.encode(user?.photoUrl.toString(), StandardCharsets.UTF_8.toString())
                                navController.navigate("${NAV_ROUTE.SUBMIT_NICKNAME.route}/${user?.uid}/${user?.displayName}/${encodedUrl}/${joinType}")
                            }
                        )
                    }
                    /** 닉네임 입력 화면 */
                    composable(
                        route = "${NAV_ROUTE.SUBMIT_NICKNAME.route}/{uid}/{nickname}/{photoUrl}/{joinType}",
                        arguments = listOf(
                            navArgument("uid") {
                                type = NavType.StringType
                            },
                            navArgument("nickname") {
                                type = NavType.StringType
                            },
                            navArgument("photoUrl") {
                                type = NavType.StringType
                            },
                            navArgument("joinType") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        SubmitNickNameScreen(
                            uid = backStackEntry.arguments?.getString("uid")?:"",
                            nickname = backStackEntry.arguments?.getString("nickname")?:"",
                            photoUrl = backStackEntry.arguments?.getString("photoUrl")?:"",
                            joinType = backStackEntry.arguments?.getString("joinType")?:"",
                            moveToScaffoldScreen = { navController.navigate(NAV_ROUTE.SCAFFOLD.route) }
                        )
                    }
                    /** 메인 화면 */
                    composable(NAV_ROUTE.SCAFFOLD.route) {
                        ScaffoldScreen(
                            mainNavController = navController,
                            selectedBottomMenuItem = selectedBottomMenuItem,
                            changeBottomMenu = { bottomMenuindex ->
                                selectedBottomMenuItem.intValue = bottomMenuindex
                            },
                            navigateToLogin = { navController.navigate(NAV_ROUTE.LOGIN.route) }
                        )
                    }
                    /** 검색 화면 */
                    composable(NAV_ROUTE.SEARCH.route) {
                        SearchScreen { navController.navigate(NAV_ROUTE.SCAFFOLD.route) }
                    }
                    /** 영화 상세 정보 화면 */
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
                            navigatePop = { navController.popBackStack() },
                            navigateToLogin = { navController.navigate(NAV_ROUTE.LOGIN.route) }
                        )
                    }
                    /** 커뮤니티 상세글 화면 */
                    composable("${NAV_ROUTE.COMMUNITY_DETAIL.route}/{communityId}") { backStackEntry ->
                        PostDetailScreen(
                            communityId = backStackEntry.arguments?.getInt("communityId") ?: 0,
                            navigatePop = { navController.popBackStack() },
                            navigateToLogin = { navController.navigate(NAV_ROUTE.LOGIN.route) }
                        )
                    }
                    /** 커뮤니티 글작성 화면 */
                    composable(NAV_ROUTE.WRITE_POST.route) {
                        WritePostScreen(
                            navigateToPostDetail = { communityId ->
                                navController.navigate("${NAV_ROUTE.COMMUNITY_DETAIL.route}/${communityId}") },
                            navigatePop = { navController.popBackStack() }
                        )
                    }
                    /** 커뮤니티 함께보기 화면 */
                    composable(WATCH_TOGETHER_MENU.CHAT_ROOM_LIST.route) {
                        ChatRoomListScreen()
                    }
                    /** 프로필 수정 화면 */
                    composable(NAV_ROUTE.PROFILE_SETTING.route) {
                        ProfileSettingScreen()
                    }
                }
            }


        }
    }
}