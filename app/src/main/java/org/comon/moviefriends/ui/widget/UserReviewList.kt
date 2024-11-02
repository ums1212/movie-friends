package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R

/** 유저 리뷰 화면 */
@Preview
@Composable
fun UserReviewList(){

    // 더미 데이터
    val reviewList = listOf(
        "유저1: 너무 재밌어요",
        "유저2: 너무 재밌어요",
        "유저3: 너무 재밌어요",
        "유저4: 너무 재밌어요",
        "유저5: 너무 재밌어요",
        "유저6: 너무 재밌어요",
        "유저7: 너무 재밌어요",
        "유저8: 너무 재밌어요",
        "유저9: 너무 재밌어요",
        "유저10: 너무 재밌어요"
    )

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp)
    ) {
        MFText(stringResource(R.string.title_user_review))
        LazyColumn(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .height(180.dp)
        ) {
            items(reviewList){ review ->
                MFText(review)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                placeholder = { Text(stringResource(R.string.hint_user_review)) },
                shape = RoundedCornerShape(12.dp),
                value = TextFieldValue(),
                onValueChange = { },
                modifier = Modifier.padding(end = 8.dp).width(250.dp),
                singleLine = true,
                enabled = true,
                keyboardActions = KeyboardActions(onDone = { }),
                )
            MFButtonWidthResizable({}, stringResource(R.string.button_user_review), 200.dp)
        }
    }
}