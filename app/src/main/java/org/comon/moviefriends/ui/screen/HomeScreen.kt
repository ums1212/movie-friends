package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.api.MovieCategory
import org.comon.moviefriends.ui.viewmodel.HomeViewModel
import org.comon.moviefriends.ui.widget.MFNavigationBar
import org.comon.moviefriends.ui.widget.MFTopAppBar
import org.comon.moviefriends.ui.widget.MovieList

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MFTopAppBar() },
        bottomBar = { MFNavigationBar() },
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
                .background(colorResource(id = R.color.friends_black)),
        ) {
            MovieCategory.entries.forEach { category ->
                MovieList(category, viewModel.sendList(category))
                Spacer(modifier = Modifier.padding(bottom = 24.dp))
            }
        }
    }


}