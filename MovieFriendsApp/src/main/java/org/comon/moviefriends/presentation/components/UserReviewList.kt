package org.comon.moviefriends.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.getTimeDiff
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.data.entity.firebase.UserReview
import org.comon.moviefriends.presentation.common.clickableOnce
import org.comon.moviefriends.presentation.viewmodel.MovieDetailViewModel

/** 유저 리뷰 화면 */
@Composable
fun UserReviewList(
    reviewList: List<UserReview?>,
    insertUserReview: (() -> Unit)? = null,
    deleteUserReview: ((String) -> Unit)? = null,
    viewModel: MovieDetailViewModel,
    user: UserInfo? = MFPreferences.getUserInfo()
){
    if(user!=null){
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val textValue = remember { mutableStateOf("") }
            OutlinedTextField(
                placeholder = { Text(stringResource(R.string.hint_user_review)) },
                shape = RoundedCornerShape(12.dp),
                value = viewModel.reviewContent,
                onValueChange = {
                    viewModel.updateReview(it)
                },
                isError = viewModel.reviewHasErrors,
                supportingText = {
                    if (viewModel.reviewHasErrors) {
                        Text(stringResource(R.string.user_review_empty))
                    }
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(250.dp),
                singleLine = true,
                enabled = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (insertUserReview != null) {
                        insertUserReview()
                    }
                    textValue.value = ""
                })
            )
            MFButtonWidthResizable({
                if (insertUserReview != null) {
                    insertUserReview()
                }
                textValue.value = ""
            }, stringResource(R.string.button_user_review), 200.dp)
        }
    }
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    HorizontalDivider()
    if(reviewList.isNotEmpty()){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(reviewList){ review ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(modifier = Modifier
                            .wrapContentSize(unbounded = true)
                            .clickableOnce { },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(end = 4.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(review?.user?.profileImage)
                                    .crossfade(true)
                                    .error(R.drawable.logo)
                                    .build(),
                                contentDescription = "회원 프로필 사진",
                                contentScale = ContentScale.Fit,
                            )
                            MFText(review?.user?.nickName ?: "")
                        }
                        if(review?.user?.id == user?.id){
                            IconButton(onClick = {
                                if (deleteUserReview != null) {
                                    review?.id?.let {
                                        deleteUserReview(it)
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "",
                                    tint = colorResource(id = R.color.friends_white)
                                )
                            }
                        }
                    }
                    MFTitle("${review?.content}")
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    MFText(text = "${review?.createdDate?.let { getTimeDiff(it.seconds) }}")
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    HorizontalDivider()
                }

            }
        }
    }else{
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        MFTitle(text = stringResource(id = R.string.no_data))
    }
}