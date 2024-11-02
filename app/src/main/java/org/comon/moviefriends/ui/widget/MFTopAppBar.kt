package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MFTopAppBar(
    route: String,
    navigateToCommunityMenu: (String) -> Unit,
    navigateToSearch: () -> Unit,
) {
    val isTabMenuShown = remember { mutableStateOf(false) }
    val selectedTabItem = remember { mutableIntStateOf(0) }

    Column {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = colorResource(R.color.friends_black),
                titleContentColor = colorResource(R.color.friends_black),
            ),
            title = {
                if(route!= NAV_ROUTE.COMMUNITY_DETAIL.route && route!= NAV_ROUTE.MOVIE_DETAIL.route){
                    Image(
                        modifier = Modifier
                            .size(width = 50.dp, height = 50.dp),
                        contentDescription = null,
                        painter = painterResource(R.drawable.logo)
                    )
                }
            },
            navigationIcon = {
                if(route== NAV_ROUTE.COMMUNITY_DETAIL.route || route== NAV_ROUTE.MOVIE_DETAIL.route){
                    IconButton(
                        onClick = {
                            // back
                        }
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
                if(route== NAV_ROUTE.COMMUNITY_DETAIL.route || COMMUNITY_MENU.entries.any { it.route == route }){
                    /** 커뮤니티 메뉴 버튼 */
                    IconButton(
                        onClick = {
                            isTabMenuShown.value = !isTabMenuShown.value
                        }
                    ) {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "커뮤니티 메뉴 버튼",
                            tint = colorResource(R.color.friends_white)
                        )
                    }
                }

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
        )

        // 탭메뉴
        if(isTabMenuShown.value){
            CommunityTab(selectedTabItem) { route, index ->
                navigateToCommunityMenu(route)
                selectedTabItem.value = index
            }
        }
    }
}