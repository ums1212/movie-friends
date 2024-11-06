package org.comon.moviefriends.presenter.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.comon.moviefriends.data.datasource.tmdb.MovieCategory
import org.comon.moviefriends.presenter.viewmodel.HomeViewModel
import org.comon.moviefriends.presenter.widget.MovieList

@Composable
fun HomeScreen(
    moveToMovieDetailScreen: (Int) -> Unit,
) {

    val viewModel: HomeViewModel = viewModel()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        MovieCategory.entries.forEach { category ->
            MovieList(category, viewModel.sendList(category)){ movieId ->
                moveToMovieDetailScreen(movieId)
            }
            Spacer(modifier = Modifier.padding(bottom = 24.dp))
        }
    }
}