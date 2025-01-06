package org.comon.moviefriends.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.common.ScaffoldNavRoute
import org.comon.moviefriends.presentation.common.checkTopBarNeedCommunityMenuButton
import org.comon.moviefriends.presentation.common.checkTopBarNeedNavigationIcon
import org.comon.moviefriends.presentation.common.checkTopBarNeedSearchButton
import org.comon.moviefriends.presentation.common.checkTopBarNeedTitle

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MFTopAppBar(
    route: String,
    selectedCommunityTabItem: MutableIntState,
    isCommunityTabMenuShown: MutableState<Boolean>,
    navigatePop: () -> Unit,
    navigateToCommunityMenu: (String) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToProfileSetting: () -> Unit,
    navigateToLogin: () -> Unit,
) {

    BackHandler(enabled = isCommunityTabMenuShown.value) {
        isCommunityTabMenuShown.value = false
    }

    Column {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = colorResource(R.color.friends_black),
                titleContentColor = colorResource(R.color.friends_black),
            ),
            title = {
                if(checkTopBarNeedTitle(route)) {
                    Image(
                        modifier = Modifier
                            .size(width = 50.dp, height = 50.dp),
                        contentDescription = null,
                        painter = painterResource(R.drawable.logo)
                    )
                }
            },
            navigationIcon = {
                if(checkTopBarNeedNavigationIcon(route)) {
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
                if(checkTopBarNeedCommunityMenuButton(route)){
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

                if(checkTopBarNeedSearchButton(route)) {
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

                if(route.contains(ScaffoldNavRoute.Profile.route)){
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