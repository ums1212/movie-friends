package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.mahmoudalim.compose_rating_bar.RatingBarView
import org.comon.moviefriends.R
import org.comon.moviefriends.api.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.common.showToast
import org.comon.moviefriends.ui.theme.FriendsRed
import org.comon.moviefriends.ui.widget.MFBottomSheet
import org.comon.moviefriends.ui.widget.MFBottomSheetContent
import org.comon.moviefriends.ui.widget.MFButton
import org.comon.moviefriends.ui.widget.MFButtonWidthResizable
import org.comon.moviefriends.ui.widget.MFText
import org.comon.moviefriends.ui.widget.RateModal
import org.comon.moviefriends.ui.widget.UserWantListItem

@Composable
fun MovieDetailScreen(movieId: Int) {

    val context = LocalContext.current

    val movieRating = remember { mutableIntStateOf(4) }
    val scrollState = rememberScrollState()

    val showRateModal = remember { mutableIntStateOf(0) }
    val showReviewBottomSheet = remember { mutableStateOf(false) }
    val showUserWantBottomSheet = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .verticalScroll(scrollState)
    ) {
        /** 영화 정보 */
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            model = ImageRequest.Builder(context)
                .data(BASE_TMDB_IMAGE_URL)
                .crossfade(true)
                .error(R.drawable.logo)
                .build(),
            contentDescription = "작품 이미지",
        )
        Spacer(Modifier.padding(vertical = 12.dp))
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MFText("작품 타이틀")
            MFText("작품 연도, 장르, 상영시간")
            MFText("작품 설명")
        }

        /** 주요 출연진 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFText(stringResource(R.string.title_credits))
        LazyRow {
            items(listOf("배우1", "배우2")) { item ->
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 4.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(BASE_TMDB_IMAGE_URL)
                            .crossfade(true)
                            .error(R.drawable.logo)
                            .build(),
                        contentDescription = "회원 프로필 사진",
                    )
                    MFText(item)
                }
            }
        }

        /** 이 영화를 보고 싶다 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFButton({
            showToast(context, "이 영화를 보고 싶다")
        }, stringResource(R.string.button_want_this_movie))
        Spacer(Modifier.padding(vertical = 12.dp))
        MFText(stringResource(R.string.title_user_want_this_movie))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyRow {
                items(listOf("유저1", "유저2")) { item ->
                    UserWantListItem()
                }
            }
            MFButtonWidthResizable({
                showUserWantBottomSheet.value = true
            }, stringResource(R.string.button_more), 90.dp)
        }

        if(showUserWantBottomSheet.value){
            MFBottomSheet(MFBottomSheetContent.UserWantList) {
                showUserWantBottomSheet.value = false
            }
        }

        /** 유저 평점 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFText(stringResource(R.string.title_user_rate))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RatingBarView(
                isRatingEditable = false,
                rating = movieRating,
                ratedStarsColor = FriendsRed,
            )
        }

        MFButton({
            showRateModal.intValue = 1
        }, stringResource(R.string.button_user_rate))

        if (showRateModal.intValue == 1) {
            RateModal {
                showRateModal.intValue = 0
            }
        }

        /** 유저 리뷰 */
        Spacer(Modifier.padding(vertical = 12.dp))
        MFText(stringResource(R.string.title_user_review))
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .height(80.dp)
        ) {
            MFText("유저1: 너무 재밌어요")
            MFText("유저1: 너무 재밌어요")
            MFText("유저1: 너무 재밌어요")
        }
        MFButton({
            showReviewBottomSheet.value = true
        }, stringResource(R.string.button_more_user_review))

        if(showReviewBottomSheet.value){
            MFBottomSheet(MFBottomSheetContent.UserReview) {
                showReviewBottomSheet.value = false
            }
        }
    }
}