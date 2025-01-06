package org.comon.moviefriends.presentation

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.comon.moviefriends.common.FullScreenNavRoute
import org.comon.moviefriends.common.IntroNavRoute
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.ScaffoldNavRoute
import org.comon.moviefriends.common.collectFlowInActivity
import org.comon.moviefriends.presentation.common.checkScreenNeedBottomBar
import org.comon.moviefriends.presentation.common.checkScreenNeedTopBar
import org.comon.moviefriends.presentation.navigation.MovieFriendsNavigation
import org.comon.moviefriends.presentation.theme.MovieFriendsTheme
import org.comon.moviefriends.presentation.viewmodel.LoginResult
import org.comon.moviefriends.presentation.viewmodel.LoginViewModel
import org.comon.moviefriends.presentation.components.MFNavigationBar
import org.comon.moviefriends.presentation.components.MFTopAppBar

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

    private val startDestination = mutableStateOf(IntroNavRoute.Login.route)

    private fun checkLogin(startDestination: MutableState<String>, loginViewModel: LoginViewModel){
        collectFlowInActivity(loginViewModel.checkLogin()){ result ->
            when(result){
                is LoginResult.Loading -> Log.d("checkLogin", "로그인여부확인로딩")
                is LoginResult.Success -> {
                    if(result.resultData) {
                        startDestination.value = ScaffoldNavRoute.Home.route
                    }else{
                        startDestination.value = IntroNavRoute.Login.route
                    }
                }
                else -> {
                    startDestination.value = IntroNavRoute.Login.route
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
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                topBar = {
                    if(checkScreenNeedTopBar(currentRoute)){
                        MFTopAppBar(
                            currentRoute ?: ScaffoldNavRoute.Home.route,
                            selectedCommunityTabItem = selectedCommunityTabItem,
                            isCommunityTabMenuShown = isCommunityTabMenuShown,
                            navigatePop =  { navController.popBackStack() },
                            navigateToCommunityMenu = { route ->
                                navController.navigate(route)
                            },
                            navigateToSearch = {
                                navController.navigate(FullScreenNavRoute.Search.route)
                            },
                            navigateToProfileSetting = {
                                navController.navigate(FullScreenNavRoute.ProfileSetting.route)
                            },
                            navigateToLogin = { navController.navigate(IntroNavRoute.Login.route) },
                        )
                    }
                },
                bottomBar = {
                    if(checkScreenNeedBottomBar(currentRoute)){
                        MFNavigationBar(
                            selectedItem = selectedBottomMenuItem,
                            navigateToLogin = { navController.navigate(IntroNavRoute.Login.route) },
                            hideCommunityTabMenu = {
                                isCommunityTabMenuShown.value = false
                            },
                            navigateToMenu = { route, index ->
                                selectedBottomMenuItem.intValue = index
                                navController.navigate(route) {
                                    // 메뉴를 선택할 때 스택에 쌓이지 않도록
                                    navController.graph.startDestinationRoute?.let {
                                        popUpTo(it) {
                                            saveState = true
                                        }
                                    }
                                    // 같은 화면이 여러개 생기지 않도록 하나의 화면을 선택하게 함
                                    launchSingleTop = true
                                    // 이전 화면을 다시 선택했을 때 이전 화면의 상태를 재사용
                                    restoreState = true
                                }
                            }
                        )
                    }
                },
            ) { innerPadding ->
                MovieFriendsNavigation(
                    navController = navController,
                    startDestination = startDestination.value,
                    innerPadding = innerPadding,
                    closeCommunityTabMenu = {
                        isCommunityTabMenuShown.value = false
                    }
                )
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