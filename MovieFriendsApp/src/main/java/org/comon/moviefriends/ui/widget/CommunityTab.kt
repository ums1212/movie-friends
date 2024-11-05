package org.comon.moviefriends.ui.widget

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

@Composable
fun CommunityTab(
    selectedItem: IntState,
    navigateToCommunityMenu: (String, Int) -> Unit
) {
    TabRow(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        selectedTabIndex = selectedItem.intValue
    ) {
        COMMUNITY_MENU.entries.forEachIndexed { index, item ->
            Tab(
                selected = selectedItem.intValue == index,
                onClick = {
                    navigateToCommunityMenu(item.route, index)
                },
                text = { Text(item.description) },
                icon = {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = item.description)
                }
            )
        }
    }
}