package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MFBottomSheet(content: MFBottomSheetContent, dismissSheet: () -> Unit) {
    ModalBottomSheet(onDismissRequest = dismissSheet) {
        when(content){
            MFBottomSheetContent.UserReview -> UserReviewList()
            MFBottomSheetContent.UserWantList -> {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    MFPostTitle(stringResource(R.string.title_user_want_this_movie))
                    UserWantThisMovieList("movie_detail")
                }
            }
        }
    }
}