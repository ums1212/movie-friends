package org.comon.moviefriends.presenter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import org.comon.moviefriends.common.CommunityCategory
import org.comon.moviefriends.presenter.common.UserAgeRange
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite

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

@Composable
fun AgeRangeModal(
    dismissModal: () -> Unit,
    getAge: (UserAgeRange) -> Unit,
){
    Dialog(
        onDismissRequest = dismissModal,
    ) {
        Column {
            UserAgeRange.entries.forEachIndexed { index, age ->
                if(index==0) return@forEachIndexed
                DropdownMenuItem(
                    modifier = Modifier.background(FriendsBoxGrey),
                    text = { Text(age.kor, color = FriendsWhite) },
                    onClick = {
                        getAge(age)
                        dismissModal()
                    },
                )
                if(index != UserAgeRange.entries.size - 1){
                    HorizontalDivider()
                }
            }
        }
    }
}