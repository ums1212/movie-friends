package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.comon.moviefriends.common.COMMUNITY_MENU
import org.comon.moviefriends.common.NAV_ROUTE
import org.comon.moviefriends.ui.widget.CommunityFab
import org.comon.moviefriends.ui.widget.MFNavigationBar
import org.comon.moviefriends.ui.widget.MFTopAppBar

@Composable
fun ScaffoldScreen(mainNavController: NavHostController){
    val scaffoldNavController = rememberNavController()
    val currentRoute = scaffoldNavController.currentBackStackEntryAsState().value?.destination?.route?.split("/")?.first()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
        NavHost(navController = scaffoldNavController, startDestination = NAV_ROUTE.HOME.route, modifier = Modifier.padding(innerPadding)) {
            composable(NAV_ROUTE.HOME.route) {
                HomeScreen { movieId ->
                    scaffoldNavController.navigate("${NAV_ROUTE.MOVIE_DETAIL.route}/${movieId}")
                }
            }
            composable("${NAV_ROUTE.MOVIE_DETAIL.route}/{movieId}") { backStackEntry ->
                MovieDetailScreen(movieId = backStackEntry.arguments?.getInt("movieId") ?: 0)
            }
            composable(COMMUNITY_MENU.COMMUNITY.route) {
                CommunityScreen({}) { communityId ->
                    scaffoldNavController.navigate("${NAV_ROUTE.COMMUNITY_DETAIL.route}/${communityId}")
                }
            }
            composable(COMMUNITY_MENU.WATCH_TOGETHER.route) {
                WatchTogetherScreen()
            }
            composable(COMMUNITY_MENU.RECOMMEND.route) {
                RecommendScreen()
            }
            composable(COMMUNITY_MENU.WORLD_CUP.route) {
                WorldCupScreen()
            }
            composable("${NAV_ROUTE.COMMUNITY_DETAIL.route}/{communityId}") { backStackEntry ->
                PostDetailScreen(communityId = backStackEntry.arguments?.getInt("communityId") ?: 0)
            }
            composable(NAV_ROUTE.WRITE_POST.route) {
                WritePostScreen()
            }
            composable(NAV_ROUTE.MY_INFO.route) {
                MyInfoScreen()
            }
        }
    }
}