package org.comon.moviefriends.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudalim.compose_rating_bar.RatingBarView
import org.comon.moviefriends.R
import org.comon.moviefriends.presentation.theme.FriendsRed
import org.comon.moviefriends.presentation.viewmodel.MovieDetailViewModel
import org.comon.moviefriends.presentation.viewmodel.UserRateState

@Composable
fun RateModal(
    viewModel: MovieDetailViewModel = hiltViewModel(),
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
                .height(250.dp)
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                RatingBarView(
                    isRatingEditable = true,
                    rating = userRate,
                    ratedStarsColor = FriendsRed,
                )
                if(userRate.intValue != 0){
                    viewModel.resetUserMovieRatingErrorMessage()
                }
                when(viewModel.userMovieRatingErrorMessage) {
                    UserRateState.Loading -> CircularProgressIndicator()
                    UserRateState.NotVote -> Text(color = Color.Red, text = stringResource(R.string.label_user_rate_empty))
                    UserRateState.Error -> Text(color = Color.Red, text = stringResource(R.string.network_error))
                    else -> {}
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    MFButtonWidthResizable(dismissModal, stringResource(R.string.button_cancel), 80.dp)
                    Spacer(Modifier.padding(end = 8.dp))
                    MFButtonConfirm(
                        clickEvent = {
                            voteUserRate(userRate.intValue)
                        },
                        text = stringResource(R.string.button_confirm),
                        width = 80.dp
                    )
                }
            }

        }
    }
}