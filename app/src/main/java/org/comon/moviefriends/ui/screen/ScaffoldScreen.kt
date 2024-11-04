package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU
import org.comon.moviefriends.ui.theme.FriendsBlack
import org.comon.moviefriends.ui.widget.CommunityFab
import org.comon.moviefriends.ui.widget.MFNavigationBar
import org.comon.moviefriends.ui.widget.MFTopAppBar

@Composable
fun ScaffoldScreen(mainNavController: NavHostController){
    val scaffoldNavController = rememberNavController()
    val currentRoute = scaffoldNavController.currentBackStackEntryAsState().value?.destination?.route?.split("/")?.first()

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
                }
            )
        },
        floatingActionButton = {
            if(currentRoute== NAV_ROUTE.COMMUNITY.route){
                CommunityFab {
                    scaffoldNavController.navigate(NAV_ROUTE.WRITE_POST.route)
                }
            }
        },
        bottomBar = {
            MFNavigationBar { route ->
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
                        scaffoldNavController.navigate("${NAV_ROUTE.MOVIE_DETAIL.route}/${movieId}")
                    }
                }
                composable(
                    route = "${NAV_ROUTE.MOVIE_DETAIL.route}/{movieId}",
                    arguments = listOf(
                        navArgument("movieId"){
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    MovieDetailScreen(movieId = backStackEntry.arguments?.getInt("movieId") ?: 0)
                }
                composable(COMMUNITY_MENU.COMMUNITY.route) {
                    CommunityScreen { communityId ->
                        scaffoldNavController.navigate("${NAV_ROUTE.COMMUNITY_DETAIL.route}/${communityId}")
                    }
                }
                composable("${NAV_ROUTE.COMMUNITY_DETAIL.route}/{communityId}") { backStackEntry ->
                    PostDetailScreen(communityId = backStackEntry.arguments?.getInt("communityId") ?: 0)
                }
                composable(NAV_ROUTE.WRITE_POST.route) {
                    WritePostScreen()
                }
                composable(COMMUNITY_MENU.WATCH_TOGETHER.route) {
                    WatchTogetherScreen(
                        navigateToRequestList = { scaffoldNavController.navigate(WATCH_TOGETHER_MENU.REQUEST_LIST.route) },
                        navigateToReceiveList = { scaffoldNavController.navigate(WATCH_TOGETHER_MENU.RECEIVE_LIST.route) },
                        navigateToChatRoomList = { scaffoldNavController.navigate(WATCH_TOGETHER_MENU.CHAT_ROOM_LIST.route) },
                        navigateToMovieDetail = { movieId ->
                            scaffoldNavController.navigate("${NAV_ROUTE.MOVIE_DETAIL.route}/${movieId}")
                        },
                    )
                }
                composable(WATCH_TOGETHER_MENU.REQUEST_LIST.route) {
                    RequestListScreen()
                }
                composable(WATCH_TOGETHER_MENU.RECEIVE_LIST.route) {
                    ReceiveListScreen()
                }
                composable(WATCH_TOGETHER_MENU.CHAT_ROOM_LIST.route) {
                    ChatRoomListScreen()
                }
                composable(COMMUNITY_MENU.RECOMMEND.route) {
                    RecommendScreen()
                }
                composable(COMMUNITY_MENU.WORLD_CUP.route) {
                    WorldCupScreen()
                }
                composable(NAV_ROUTE.MY_INFO.route) {
                    MyInfoScreen()
                }
            }
        }
    }
}