package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.ui.widget.MFText

@Composable
fun CommunityScreen(
    moveToCommunityDetailScreen: (Int) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp)
            .background(colorResource(id = R.color.friends_black)),
    ) {
        MFText(stringResource(R.string.label_menu_community))
        Card(
            modifier = Modifier.fillMaxWidth()
                .border(border = BorderStroke(1.dp, colorResource(id = R.color.friends_red)), shape = RoundedCornerShape(12.dp)),
            onClick = {
                moveToCommunityDetailScreen(0)
            }
        ) {
            MFText(stringResource(R.string.label_community_notification))
        }
    }
}