package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.api.MovieCategory
import org.comon.moviefriends.ui.viewmodel.HomeViewModel
import org.comon.moviefriends.ui.widget.MovieList

@Composable
fun HomeScreen(innerPadding: PaddingValues, viewModel: HomeViewModel) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(innerPadding)
    ) {
        MovieList(MovieCategory.NOW_PLAYING, viewModel.nowList)
        Spacer(modifier = Modifier.padding(bottom = 24.dp))
        MovieList(MovieCategory.POPULAR, viewModel.popList)
        Spacer(modifier = Modifier.padding(bottom = 24.dp))
        MovieList(MovieCategory.TOP_RATED, viewModel.topList)
        Spacer(modifier = Modifier.padding(bottom = 24.dp))
        MovieList(MovieCategory.UP_COMING, viewModel.upcomingList)
    }
}