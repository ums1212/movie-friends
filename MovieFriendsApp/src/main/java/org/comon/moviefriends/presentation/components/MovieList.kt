package org.comon.moviefriends.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import kotlinx.coroutines.flow.StateFlow
import org.comon.moviefriends.R
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.datasource.tmdb.BASE_TMDB_IMAGE_URL
import org.comon.moviefriends.data.datasource.tmdb.MovieCategory
import org.comon.moviefriends.data.entity.tmdb.ResponseMoviesDto
import org.comon.moviefriends.presentation.common.clickableOnce
import retrofit2.Response


@Composable
fun MovieList(
    category: MovieCategory,
    showErrorSnackBar: () -> Unit,
    responseList: StateFlow<APIResult<Response<ResponseMoviesDto>>>,
    onNavigateToMovieDetail: (movieInfo: ResponseMoviesDto.MovieInfo) -> Unit
) {

    val stateLists = responseList.collectAsStateWithLifecycle()

    Column(
        Modifier.height(280.dp)
    ) {
        MFTitle(text = category.str)
        when(val list = stateLists.value){
            APIResult.NoConstructor -> Log.d("TAG", "MovieList: NoConstructor")
            APIResult.Loading -> Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ShimmerEffect(modifier = Modifier.width(150.dp).height(200.dp).padding(8.dp))
                ShimmerEffect(modifier = Modifier.width(150.dp).height(200.dp).padding(8.dp))
                ShimmerEffect(modifier = Modifier.width(150.dp).height(200.dp).padding(8.dp))
                ShimmerEffect(modifier = Modifier.width(150.dp).height(200.dp).padding(8.dp))
            }
            is APIResult.NetworkError -> showErrorSnackBar()
            is APIResult.Success -> {
                LazyRow(
                    Modifier.fillMaxWidth()
                ) {
                    list.resultData.body()?.let {
                        items(it.results) { item ->
                            Column(
                                modifier = Modifier
                                    .width(160.dp)
                                    .clickableOnce { onNavigateToMovieDetail(item) },
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
                                        modifier = Modifier.fillMaxSize(),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data("$BASE_TMDB_IMAGE_URL${item.posterPath}")
                                            .crossfade(true)
                                            .error(R.drawable.yoshicat)
                                            .build(),
                                        contentDescription = "작품 정보",
                                        contentScale = ContentScale.Fit
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
                    } ?: run {
                        showErrorSnackBar()
                        items(4) {
                            ShimmerEffect(modifier = Modifier.width(150.dp).height(200.dp).padding(8.dp))
                        }
                    }
                }
            }
        }
    }


}