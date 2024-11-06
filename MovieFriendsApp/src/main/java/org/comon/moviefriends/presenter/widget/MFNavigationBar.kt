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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import org.comon.moviefriends.R
import org.comon.moviefriends.common.NAV_MENU

@Composable
fun MFNavigationBar(
    selectedItem: IntState,
    navigateToMenu: (String, Int) -> Unit,
) {

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
                    navigateToMenu(NAV_MENU.entries[index].route, index)
                }
            )
        }
    }
}