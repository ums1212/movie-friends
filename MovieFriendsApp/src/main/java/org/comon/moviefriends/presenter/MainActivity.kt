package org.comon.moviefriends.presenter

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.presenter.screen.intro.LoginScreen
import org.comon.moviefriends.common.NAV_ROUTE
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.common.collectFlowInActivity
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
import org.comon.moviefriends.presenter.viewmodel.JoinType
import org.comon.moviefriends.presenter.viewmodel.LoginResult
import org.comon.moviefriends.presenter.viewmodel.LoginViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // super.onCreate() 이전에 installSplashScreen() 메서드를 추가
        val splashScreen = installSplashScreen()
        settingSplashScreenAnimation()
        super.onCreate(savedInstanceState)
        checkLoginAndStart(splashScreen, loginViewModel)
        MFPreferences.init(this)
        alertNotificationPermission()
//        enableEdgeToEdge()

        // 상단 알림바를 클릭하여 들어온 경우 받은 내역 화면으로 이동해야 함
        val fromFCMRoute = intent.getStringExtra("fromFCMRoute")

        setContent {
            MovieFriendsApp(startDestination, fromFCMRoute)
        }
    }

    private fun checkLoginAndStart(splashScreen: SplashScreen, loginViewModel: LoginViewModel){
        checkLogin(startDestination, loginViewModel)
        collectFlowInActivity(loginViewModel.splashScreenState) {
            splashScreen.setKeepOnScreenCondition {
                it
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

    private val startDestination = mutableStateOf(NAV_ROUTE.LOGIN.route)

    private fun checkLogin(startDestination: MutableState<String>, loginViewModel: LoginViewModel){
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
        startDestination: MutableState<String>,
        fromFCMRoute2: String? = null,
    ){
        MovieFriendsTheme(
            darkTheme = true
        ) {
            val navController = rememberNavController()
            val selectedBottomMenuItem = remember {
                if(fromFCMRoute2!=null) mutableIntStateOf(1) else mutableIntStateOf(0)
            }
            val selectedCommunityTabItem = remember { mutableIntStateOf(0) }
            val isCommunityTabMenuShown = remember { mutableStateOf(false) }

            Box(modifier = Modifier
                .background(FriendsBlack)
                .fillMaxSize()){
                NavHost(navController = navController, startDestination = startDestination.value) {
                    /** 로그인 화면 */
                    composable(NAV_ROUTE.LOGIN.route) {
                        LoginScreen(
                            { navController.navigate(NAV_ROUTE.SCAFFOLD.route) },
                            { user, joinType ->
                                when(joinType){
                                    JoinType.KAKAO.str -> {
                                        val encodedUrl = URLEncoder.encode(user?.photoUrl.toString(), StandardCharsets.UTF_8.toString())
                                        navController.navigate("${NAV_ROUTE.SUBMIT_NICKNAME.route}/${user?.uid}/${user?.displayName}/${encodedUrl}/${joinType}")
                                    }
                                    JoinType.GOOGLE.str -> {
                                        val encodedUrl = URLEncoder.encode(user?.photoUrl.toString(), StandardCharsets.UTF_8.toString())
                                        navController.navigate("${NAV_ROUTE.SUBMIT_NICKNAME.route}/${user?.uid}//${encodedUrl}/${joinType}")
                                    }
                                }

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
                            selectedCommunityTabItem = selectedCommunityTabItem,
                            isCommunityTabMenuShown = isCommunityTabMenuShown,
                            navigateToLogin = { navController.navigate(NAV_ROUTE.LOGIN.route) } ,
                            fromFCMRoute = fromFCMRoute2,
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
                    composable("${NAV_ROUTE.COMMUNITY_DETAIL.route}/{postId}") { backStackEntry ->
                        PostDetailScreen(
                            postId = backStackEntry.arguments?.getString("postId") ?: "",
                            navigatePop = { navController.popBackStack() },
                        )
                    }
                    /** 커뮤니티 글작성 화면 */
                    composable(NAV_ROUTE.WRITE_POST.route) {
                        WritePostScreen(
                            navigateToPostDetail = { postId ->
                                navController.navigate("${NAV_ROUTE.COMMUNITY_DETAIL.route}/${postId}"){
                                    popUpTo(NAV_ROUTE.COMMUNITY_DETAIL.route){ inclusive = true}
                                }
                            },
                            navigatePop = { navController.popBackStack() },
                            postId = null
                        )
                    }
                    /** 커뮤니티 글수정 화면 */
                    composable("${NAV_ROUTE.WRITE_POST.route}/{postId}") { backStackEntry ->
                        val postId = backStackEntry.arguments?.getString("postId") ?: ""
                        WritePostScreen(
                            navigateToPostDetail = {
                                navController.navigate("${NAV_ROUTE.COMMUNITY_DETAIL.route}/${postId}"){
                                    popUpTo(NAV_ROUTE.COMMUNITY_DETAIL.route){ inclusive = true}
                                }
                            },
                            navigatePop = { navController.popBackStack() },
                            postId = postId
                        )
                    }
                    /** 커뮤니티 채팅방 화면 */
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

    // 사용자 허락시 동작할 코드
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 알림 권한 승인
            MFPreferences.setNotiPermission(true)
        } else {
            // 알림 권한 미승인
            MFPreferences.setNotiPermission(false)
        }
    }

    private fun alertNotificationPermission() {
        // API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // "사용자에게 이 권한이 왜 필요한지 설명하는 화면이 필요할 시!"
            } else {
                // 직접 퍼미션 요청
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}