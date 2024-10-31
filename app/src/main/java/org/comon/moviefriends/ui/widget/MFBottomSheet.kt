package org.comon.moviefriends.ui.widget

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MFBottomSheet(dismissSheet: () -> Unit) {
    ModalBottomSheet(onDismissRequest = dismissSheet) {
        UserReviewList()
    }
}