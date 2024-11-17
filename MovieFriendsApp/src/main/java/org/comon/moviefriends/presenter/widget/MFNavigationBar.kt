package org.comon.moviefriends.presenter.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.ui.res.colorResource
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.NAV_MENU
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.presenter.screen.profile.ProfileType

@Composable
fun MFNavigationBar(
    currentRoute: String?,
    selectedItem: IntState,
    navigateToLogin: () -> Unit,
    hideCommunityTabMenu: () -> Unit,
    navigateToMenu: (String, Int) -> Unit,
) {
    val user = MFPreferences.getUserInfo()

    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Create,
        Icons.Filled.AccountBox
    )

    NavigationBar(
        containerColor = colorResource(R.color.friends_black),
        contentColor = colorResource(R.color.friends_black),
    ) {
        NAV_MENU.entries.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        icons[index],
                        contentDescription = item.description
                    )
                },
                label = { Text(item.description) },
                selected = selectedItem.intValue == index,
                onClick = {
                    hideCommunityTabMenu()
                    clickNavigationBarItem(user, index, navigateToLogin, navigateToMenu)
                }
            )
        }
    }
}

fun clickNavigationBarItem(user: UserInfo?, index: Int, navigateToLogin: () -> Unit, navigateToMenu: (String, Int) -> Unit){
    navigateToMenu(
        if(NAV_MENU.entries[index].route==NAV_MENU.PROFILE.route){
            if(user==null){
                navigateToLogin()
                return
            }
            "${NAV_MENU.PROFILE.route}/${ProfileType.MY_INFO.str}"
        }else{
            NAV_MENU.entries[index].route
        }, index)
}