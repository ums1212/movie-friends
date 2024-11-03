package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.api.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.ui.theme.FriendsBlack
import org.comon.moviefriends.ui.widget.MFButtonWidthResizable
import org.comon.moviefriends.ui.widget.MFText
import org.comon.moviefriends.ui.widget.UserWantListItem

@Preview
@Composable
fun WatchTogetherScreen() {

    val watchTogetherList = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) }

    Column(
        modifier = Modifier.fillMaxWidth().background(FriendsBlack),
    ) {
        MFText(text = stringResource(R.string.label_menu_watch_together))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider()
        listOf(
            stringResource(R.string.label_request_list),
            stringResource(R.string.label_receive_list),
            stringResource(R.string.label_chat_room)
        ).forEach { menu ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {  },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MFText(menu)
            }
            HorizontalDivider()
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        MFText(text = stringResource(R.string.title_user_want_movies))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(watchTogetherList){ item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .width(80.dp)
                                .height(120.dp)
                                .padding(4.dp)
                                .clickable {  },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp,
                                pressedElevation = 16.dp
                            )
                        ) {
                            AsyncImage(
                                modifier = Modifier.fillMaxWidth(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("$BASE_TMDB_IMAGE_URL")
                                    .error(R.drawable.logo)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "작품 정보",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                        UserWantListItem()
                    }
                    MFButtonWidthResizable({}, stringResource(R.string.button_watch_together), 120.dp)
                }
            }
        }
    }
}