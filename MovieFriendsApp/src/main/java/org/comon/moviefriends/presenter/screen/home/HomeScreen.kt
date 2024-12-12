package org.comon.moviefriends.presenter.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collect
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.MovieCategory
import org.comon.moviefriends.presenter.viewmodel.HomeViewModel
import org.comon.moviefriends.presenter.components.MovieList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    moveToMovieDetailScreen: (Int) -> Unit
) {

    val scrollState = rememberScrollState()
    val localContext = LocalContext.current
    val snackBarHost = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllMovies().collect()
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
        ) {
            MovieCategory.entries.forEach { category ->
                MovieList(
                    category = category,
                    showErrorSnackBar = { showSnackBar(coroutineScope, snackBarHost, localContext) },
                    list = viewModel.sendList(category),
                    onNavigateToMovieDetail = { movieId ->
                        moveToMovieDetailScreen(movieId)
                    }
                )
                Spacer(modifier = Modifier.padding(bottom = 24.dp))
            }
        }
        SnackbarHost(snackBarHost)
    }

}