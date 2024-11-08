package org.comon.moviefriends.presenter.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.common.COMMUNITY_MENU
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.model.UserInfo

@Composable
fun CommunityTab(
    selectedItem: IntState,
    navigateToCommunityMenu: (String, Int) -> Unit,
    navigateToLogin: () -> Unit,
) {
    val localContext = LocalContext.current
    val user = MFPreferences.getUserInfo(localContext)
    TabRow(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        selectedTabIndex = selectedItem.intValue
    ) {
        COMMUNITY_MENU.entries.forEachIndexed { index, item ->
            Tab(
                selected = selectedItem.intValue == index,
                onClick = {
                    clickCommunityTab(
                        user = user,
                        navigateToLogin = navigateToLogin,
                        route = item.route,
                        index = index,
                        navigateToCommunityMenu = navigateToCommunityMenu
                    )
                },
                text = { Text(item.description) },
                icon = {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = item.description)
                }
            )
        }
    }
}

fun clickCommunityTab(
    user: UserInfo?,
    navigateToLogin: () -> Unit,
    route: String,
    index: Int,
    navigateToCommunityMenu: (String, Int) -> Unit
){
    if(user==null){
        if(route == COMMUNITY_MENU.WATCH_TOGETHER.route || route == COMMUNITY_MENU.RECOMMEND.route){
            navigateToLogin()
            return
        }
    }
    navigateToCommunityMenu(route, index)
}