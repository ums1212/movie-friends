package org.comon.moviefriends.presenter.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import org.comon.moviefriends.R
import org.comon.moviefriends.common.NAV_ROUTE
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.data.model.firebase.UserReview
import org.comon.moviefriends.data.model.firebase.UserWantMovieInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MFBottomSheet(
    content: MFBottomSheetContent,
    dismissSheet: () -> Unit,
    userWantList: List<UserWantMovieInfo?>? = null,
    requestWatchTogether: ((UserInfo) -> Result<Task<QuerySnapshot>>)? = null,
    userReviewList: APIResult<List<UserReview?>>? = null,
    insertUserReview: ((String) -> Unit)? = null,
    deleteUserReview: ((String) -> Unit)? = null,
    ) {
    ModalBottomSheet(onDismissRequest = dismissSheet) {
        when(content){
            /** 이 영화를 보고 싶은 사람 */
            MFBottomSheetContent.UserWantList -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    MFPostTitle(stringResource(R.string.title_user_want_this_movie))
                    if (userWantList != null && requestWatchTogether != null) {
                        UserWantThisMovieList(
                            screen = NAV_ROUTE.MOVIE_DETAIL.route,
                            wantList = userWantList,
                            navigateToMovieDetail = null,
                            requestWatchTogether = { receiveUser -> requestWatchTogether(receiveUser) }
                        )
                    }else{
                        Text(text = stringResource(id = R.string.network_error))
                    }
                }
            }
            /** 유저 리뷰 */
            MFBottomSheetContent.UserReview -> userReviewList?.let { UserReviewList(it, insertUserReview, deleteUserReview) }

        }
    }
}