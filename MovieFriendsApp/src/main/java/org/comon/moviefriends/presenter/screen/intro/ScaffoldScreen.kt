package org.comon.moviefriends.presenter.screen.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.comon.moviefriends.common.COMMUNITY_MENU
import org.comon.moviefriends.common.NAV_ROUTE
import org.comon.moviefriends.common.PROFILE_MENU
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.presenter.screen.community.CommunityScreen
import org.comon.moviefriends.presenter.screen.community.ReceiveListScreen
import org.comon.moviefriends.presenter.screen.community.RecommendScreen
import org.comon.moviefriends.presenter.screen.community.RequestListScreen
import org.comon.moviefriends.presenter.screen.community.WatchTogetherScreen
import org.comon.moviefriends.presenter.screen.community.WorldCupScreen
import org.comon.moviefriends.presenter.screen.home.HomeScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileCommunityPostScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileCommunityReplyScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileRateScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileReviewScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileSettingScreen
import org.comon.moviefriends.presenter.screen.profile.ProfileWantMovieScreen
import org.comon.moviefriends.presenter.theme.FriendsBlack
import org.comon.moviefriends.presenter.widget.MFNavigationBar
import org.comon.moviefriends.presenter.widget.MFTopAppBar

@Composable
fun ScaffoldScreen(
    mainNavController: NavHostController,
    selectedBottomMenuItem: IntState,
    changeBottomMenu: (Int) -> Unit,
    navigateToLogin: () -> Unit
){
    val scaffoldNavController = rememberNavController()
    val currentRoute = scaffoldNavController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            MFTopAppBar(
                currentRoute ?: NAV_ROUTE.HOME.route,
                navigatePop =  { scaffoldNavController.popBackStack() },
                navigateToCommunityMenu = { route ->
                    scaffoldNavController.navigate(route)
                },
                navigateToSearch = {
                    mainNavController.navigate(NAV_ROUTE.SEARCH.route)
                },
                confirmPost = {
                    scaffoldNavController.popBackStack()
                },
                navigateToProfileSetting = {
                    mainNavController.navigate(NAV_ROUTE.PROFILE_SETTING.route)
                },
                navigateToLogin = navigateToLogin
            )
        },
        bottomBar = {
            MFNavigationBar(
                selectedItem = selectedBottomMenuItem,
                navigateToLogin = navigateToLogin,
                navigateToMenu = { route, index ->
                    changeBottomMenu(index)
                    scaffoldNavController.navigate(route) {
                        scaffoldNavController.graph.startDestinationRoute?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.background(FriendsBlack)){
            NavHost(
                navController = scaffoldNavController,
                startDestination = NAV_ROUTE.HOME.route,
                modifier = Modifier.padding(innerPadding).padding(start = 12.dp, end = 12.dp, top = 12.dp)
            ) {
                composable(NAV_ROUTE.HOME.route) {
                    HomeScreen { movieId ->
                        mainNavController.navigate("${NAV_ROUTE.MOVIE_DETAIL.route}/${movieId}")
                    }
                }
                composable(COMMUNITY_MENU.COMMUNITY.route) {
                    CommunityScreen(
                        moveToCommunityDetailScreen = { postId ->
                            mainNavController.navigate("${NAV_ROUTE.COMMUNITY_DETAIL.route}/${postId}")
                        },
                        moveToWritePostScreen = { mainNavController.navigate(NAV_ROUTE.WRITE_POST.route) },
                        navigateToLogin = navigateToLogin
                    )
                }
                composable(COMMUNITY_MENU.WATCH_TOGETHER.route) {
                    WatchTogetherScreen(
                        navigateToRequestList = { scaffoldNavController.navigate(WATCH_TOGETHER_MENU.REQUEST_LIST.route) },
                        navigateToReceiveList = { scaffoldNavController.navigate(WATCH_TOGETHER_MENU.RECEIVE_LIST.route) },
                        navigateToChatRoomList = { mainNavController.navigate(WATCH_TOGETHER_MENU.CHAT_ROOM_LIST.route) },
                        navigateToMovieDetail = { movieId ->
                            mainNavController.navigate("${NAV_ROUTE.MOVIE_DETAIL.route}/${movieId}")
                        },
                    )
                }
                composable(WATCH_TOGETHER_MENU.REQUEST_LIST.route) {
                    RequestListScreen(
                        navigateToMovieDetail = { movieId -> mainNavController.navigate("${NAV_ROUTE.MOVIE_DETAIL.route}/${movieId}")}
                    )
                }
                composable(WATCH_TOGETHER_MENU.RECEIVE_LIST.route) {
                    ReceiveListScreen(
                        navigateToMovieDetail = { movieId -> mainNavController.navigate("${NAV_ROUTE.MOVIE_DETAIL.route}/${movieId}")}
                    )
                }
                composable(COMMUNITY_MENU.RECOMMEND.route) {
                    RecommendScreen()
                }
                composable(COMMUNITY_MENU.WORLD_CUP.route) {
                    WorldCupScreen()
                }
                composable(
                    route = "${NAV_ROUTE.PROFILE.route}/{profileType}",
                    arguments = listOf(
                        navArgument("profileType"){
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    ProfileScreen(
                        navigateToUserWant = { scaffoldNavController.navigate(PROFILE_MENU.PROFILE_WANT_MOVIE.route) },
                        navigateToUserRate = { scaffoldNavController.navigate(PROFILE_MENU.PROFILE_RATE.route) },
                        navigateToUserReview = { scaffoldNavController.navigate(PROFILE_MENU.PROFILE_REVIEW.route) },
                        navigateToUserCommunityPost = { scaffoldNavController.navigate(PROFILE_MENU.PROFILE_COMMUNITY_POST.route) },
                        navigateToUserCommunityReply = { scaffoldNavController.navigate(PROFILE_MENU.PROFILE_COMMUNITY_REPLY.route) },
                        profileType = backStackEntry.arguments?.getString("profileType") ?: "",
                    )
                }
                composable(NAV_ROUTE.PROFILE_SETTING.route) {
                    ProfileSettingScreen()
                }
                composable(PROFILE_MENU.PROFILE_WANT_MOVIE.route) {
                    ProfileWantMovieScreen(
                        navigateToLogin = { mainNavController.navigate(NAV_ROUTE.LOGIN.route) }
                    )
                }
                composable(PROFILE_MENU.PROFILE_RATE.route) {
                    ProfileRateScreen()
                }
                composable(PROFILE_MENU.PROFILE_REVIEW.route) {
                    ProfileReviewScreen()
                }
                composable(PROFILE_MENU.PROFILE_COMMUNITY_POST.route) {
                    ProfileCommunityPostScreen{ postId ->
                        mainNavController.navigate("${NAV_ROUTE.COMMUNITY_DETAIL.route}/${postId}")
                    }
                }
                composable(PROFILE_MENU.PROFILE_COMMUNITY_REPLY.route) {
                    ProfileCommunityReplyScreen{ postId ->
                        mainNavController.navigate("${NAV_ROUTE.COMMUNITY_DETAIL.route}/${postId}")
                    }
                }
            }
        }
    }
}