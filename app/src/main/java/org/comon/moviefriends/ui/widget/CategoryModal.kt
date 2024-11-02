package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import org.comon.moviefriends.common.CommunityCategory
import org.comon.moviefriends.ui.theme.FriendsBoxGrey
import org.comon.moviefriends.ui.theme.FriendsWhite

@Composable
fun CategoryModal(
    dismissModal: () -> Unit,
    getCategory: (CommunityCategory) -> Unit,
){
    Dialog(
        onDismissRequest = dismissModal,
    ) {
        Column {
            CommunityCategory.entries.forEachIndexed { index, category ->
                DropdownMenuItem(
                    modifier = Modifier.background(FriendsBoxGrey),
                    text = { Text(category.kor, color = FriendsWhite) },
                    onClick = {
                        getCategory(category)
                        dismissModal()
                    },
                )
                if(index != CommunityCategory.entries.size - 1){
                    HorizontalDivider()
                }
            }
        }
    }
}