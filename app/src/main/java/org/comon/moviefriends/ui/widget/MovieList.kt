package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import org.comon.moviefriends.R
import org.comon.moviefriends.api.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.api.MovieCategory
import org.comon.moviefriends.model.TMDBMovies


@Composable
fun MovieList(category: MovieCategory, list: StateFlow<List<TMDBMovies.MovieInfo>>, onNavigateToMovieDetail: (movieId: Int) -> Unit) {

    val isLoading = remember { mutableStateOf(true) }
    val mutableList = remember { listOf<TMDBMovies.MovieInfo>() }
    val stateLists = remember { mutableStateOf(mutableList) }

    LaunchedEffect(isLoading.value){
        list.collectLatest {
            stateLists.value = it
        }
    }

    Column(
        Modifier.height(280.dp)
    ) {
        Text(text = category.str)
        LazyRow(
            Modifier.fillMaxWidth()
        ) {
            items(stateLists.value) { item ->
                Column(
                    modifier = Modifier
                        .width(160.dp)
                        .clickable { onNavigateToMovieDetail(item.id) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Card(
                        modifier = Modifier
                            .width(150.dp)
                            .height(200.dp)
                            .padding(8.dp),
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
                                .data("${BASE_TMDB_IMAGE_URL}${item.posterPath}")
                                .crossfade(true)
                                .build(),
                            contentDescription = "작품 정보",
                        )
                    }
                    Text(
                        text = item.title,
                        color = colorResource(id = R.color.friends_white),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        )
                    Text(
                        text = item.releaseDate,
                        color = colorResource(id = R.color.friends_white),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        )
                }
            }
        }
    }


}