package org.comon.moviefriends.presenter.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.comon.moviefriends.data.datasource.tmdb.MovieCategory
import org.comon.moviefriends.presenter.viewmodel.HomeViewModel
import org.comon.moviefriends.presenter.widget.MovieList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    moveToMovieDetailScreen: (Int) -> Unit
) {

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllMovies()
    }

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