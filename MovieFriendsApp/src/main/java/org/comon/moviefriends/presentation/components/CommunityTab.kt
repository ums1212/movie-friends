package org.comon.moviefriends.presentation.components

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
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.common.COMMUNITY_MENU
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.presentation.common.checkCommunityTabItemNeedLogin

@Composable
fun CommunityTab(
    selectedItem: IntState,
    navigateToCommunityMenu: (String, Int) -> Unit,
    navigateToLogin: () -> Unit,
) {
    TabRow(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        selectedTabIndex = selectedItem.intValue
    ) {
        COMMUNITY_MENU.entries.forEachIndexed { index, item ->
            Tab(
                selected = selectedItem.intValue == index,
                onClick = {
                    clickCommunityTab(
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
    navigateToLogin: () -> Unit,
    route: String,
    index: Int,
    navigateToCommunityMenu: (String, Int) -> Unit
){
    if(checkCommunityTabItemNeedLogin(route, MFPreferences.getUserInfo())){
        navigateToLogin()
        return
    }
    navigateToCommunityMenu(route, index)
}