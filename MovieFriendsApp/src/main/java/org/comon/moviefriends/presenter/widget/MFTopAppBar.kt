package org.comon.moviefriends.presenter.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.common.COMMUNITY_MENU
import org.comon.moviefriends.common.NAV_ROUTE
import org.comon.moviefriends.common.PROFILE_MENU
import org.comon.moviefriends.common.WATCH_TOGETHER_MENU

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MFTopAppBar(
    route: String,
    selectedCommunityTabItem: MutableIntState,
    isCommunityTabMenuShown: MutableState<Boolean>,
    navigatePop: () -> Unit,
    navigateToCommunityMenu: (String) -> Unit,
    navigateToSearch: () -> Unit,
    confirmPost: () -> Unit,
    navigateToProfileSetting: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val isTabMenuShown = remember { mutableStateOf(false) }

    Column {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = colorResource(R.color.friends_black),
                titleContentColor = colorResource(R.color.friends_black),
            ),
            title = {
                if(
                    !route.contains(NAV_ROUTE.COMMUNITY_DETAIL.route)
                    && !route.contains(NAV_ROUTE.MOVIE_DETAIL.route)
                    && !PROFILE_MENU.entries.any { route.contains(it.route) }
                    && !WATCH_TOGETHER_MENU.entries.any { route.contains(it.route)}
                    ) {
                    Image(
                        modifier = Modifier
                            .size(width = 50.dp, height = 50.dp),
                        contentDescription = null,
                        painter = painterResource(R.drawable.logo)
                    )
                }
            },
            navigationIcon = {
                if(route.contains(NAV_ROUTE.COMMUNITY_DETAIL.route)
                    || route.contains(NAV_ROUTE.MOVIE_DETAIL.route)
                    || route.contains(NAV_ROUTE.WRITE_POST.route)
                    || WATCH_TOGETHER_MENU.entries.any { route.contains(it.route)}
                    || PROFILE_MENU.entries.any { route.contains(it.route)  }
                ) {
                    IconButton(
                        onClick = navigatePop
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기 버튼",
                            tint = colorResource(R.color.friends_white)
                        )
                    }
                }
            },
            actions = {
                if(route.contains(NAV_ROUTE.COMMUNITY_DETAIL.route) || COMMUNITY_MENU.entries.any { route.contains(it.route)  }){
                    /** 커뮤니티 메뉴 버튼 */
                    IconButton(
                        onClick = {
                            isCommunityTabMenuShown.value = !isCommunityTabMenuShown.value
                        }
                    ) {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "커뮤니티 메뉴 버튼",
                            tint = colorResource(R.color.friends_white)
                        )
                    }
                }
                if(route== NAV_ROUTE.HOME.route
                    || route== NAV_ROUTE.MOVIE_DETAIL.route
                    || route== NAV_ROUTE.COMMUNITY.route
                    || route== NAV_ROUTE.COMMUNITY_DETAIL.route
                    || route== COMMUNITY_MENU.WATCH_TOGETHER.route
                    || route== COMMUNITY_MENU.RECOMMEND.route
                    || route== COMMUNITY_MENU.WORLD_CUP.route
                    ){
                    /** 검색 버튼 */
                    IconButton(
                        onClick = navigateToSearch
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "검색 버튼",
                            tint = colorResource(R.color.friends_white)
                        )
                    }
                }



                if(route== NAV_ROUTE.WRITE_POST.route){
                    /** 글 작성 버튼 */
                    IconButton(
                        onClick = confirmPost,
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "글 작성 버튼",
                            tint = colorResource(R.color.friends_white)
                        )
                    }
                }

                if(route.contains(NAV_ROUTE.PROFILE.route)){
                    /** 프로필 변경 버튼 */
                    IconButton(
                        onClick = navigateToProfileSetting,
                    ) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "프로필 변경 버튼",
                            tint = colorResource(R.color.friends_white)
                        )
                    }
                }

            }
        )

        // 탭메뉴
        if(isCommunityTabMenuShown.value){
            CommunityTab(
                selectedItem = selectedCommunityTabItem,
                navigateToCommunityMenu = { route, index ->
                    navigateToCommunityMenu(route)
                    selectedCommunityTabItem.intValue = index
                    isCommunityTabMenuShown.value = false
                },
                navigateToLogin = navigateToLogin
            )
        }
    }
}