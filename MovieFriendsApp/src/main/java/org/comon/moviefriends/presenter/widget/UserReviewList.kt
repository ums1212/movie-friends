package org.comon.moviefriends.presenter.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.UserReview

/** 유저 리뷰 화면 */
@Composable
fun UserReviewList(
    reviewList: APIResult<List<UserReview?>>,
    insertUserReview: ((String) -> Unit)? = null,
){
    val user = MFPreferences.getUserInfo()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        MFPostTitle(stringResource(R.string.title_user_review))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        if(user!=null){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val textValue = remember { mutableStateOf("") }
                OutlinedTextField(
                    placeholder = { Text(stringResource(R.string.hint_user_review)) },
                    shape = RoundedCornerShape(12.dp),
                    value = textValue.value,
                    onValueChange = {
                        textValue.value = it
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(250.dp),
                    singleLine = true,
                    enabled = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (insertUserReview != null) {
                            insertUserReview(textValue.value)
                        }
                        textValue.value = ""
                    })
                )
                MFButtonWidthResizable({
                    if (insertUserReview != null) {
                        insertUserReview(textValue.value)
                    }
                    textValue.value = ""
                }, stringResource(R.string.button_user_review), 200.dp)
            }
        }
        when(reviewList){
            APIResult.Loading -> {
                CircularProgressIndicator()
            }
            is APIResult.Success -> {
                if(reviewList.resultData.isNotEmpty()){
                    LazyColumn(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize()
                    ) {
                        items(reviewList.resultData){ review ->
                            MFPostTitle("${review?.user?.nickName} : ${review?.content}")
                        }
                    }
                }else{
                    MFPostTitle(text = stringResource(id = R.string.no_data))
                }
            }
            is APIResult.NetworkError -> MFPostTitle(text = stringResource(id = R.string.no_data))
            else -> {}
        }
    }
}