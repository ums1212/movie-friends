package org.comon.moviefriends.presenter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mahmoudalim.compose_rating_bar.RatingBarView
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.theme.FriendsRed

@Composable
fun RateModal(
    dismissModal: () -> Unit,
    userRate: MutableIntState,
    voteUserRate: (star: Int) -> Unit,
) {

    Dialog(
        onDismissRequest = dismissModal,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                text = stringResource(R.string.label_user_rate),
                modifier = Modifier
                    .padding(top=12.dp, start=16.dp, bottom = 8.dp),
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RatingBarView(
                    isRatingEditable = true,
                    rating = userRate,
                    ratedStarsColor = FriendsRed,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    MFButtonWidthResizable(dismissModal, stringResource(R.string.button_cancel), 80.dp)
                    Spacer(Modifier.padding(end = 8.dp))
                    MFButtonConfirm(
                        {
                            voteUserRate(userRate.intValue)
                        dismissModal() },
                        stringResource(R.string.button_confirm),
                        80.dp
                    )
                }
            }

        }
    }
}