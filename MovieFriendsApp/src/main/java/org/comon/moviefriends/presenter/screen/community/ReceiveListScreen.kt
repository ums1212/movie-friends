package org.comon.moviefriends.presenter.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.presenter.components.MFPostTitle
import org.comon.moviefriends.presenter.components.MFText
import org.comon.moviefriends.presenter.components.ReceiveListItem
import org.comon.moviefriends.presenter.components.ShimmerEffect
import org.comon.moviefriends.presenter.theme.FriendsBlack
import org.comon.moviefriends.presenter.viewmodel.MovieDetailViewModel

@Composable
fun ReceiveListScreen(
    navigateToMovieDetail: (id:Int) -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserInfo(MFPreferences.getUserInfo())
        viewModel.getMyReceiveList()
    }

    val localContext = LocalContext.current
    val snackBarHost = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val receiveList = viewModel.myChatReceiveList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FriendsBlack),
    ) {
        MFPostTitle(text = stringResource(R.string.label_receive_list), modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider()
        SnackbarHost(hostState = snackBarHost)
        when(val list = receiveList.value){
            APIResult.Loading -> ReceiveListShimmer()
            is APIResult.NetworkError -> {
                showSnackBar(coroutineScope, snackBarHost, localContext)
                Box(Modifier.fillMaxSize()){
                    MFText(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.network_error))
                }
            }
            is APIResult.Success -> {
                if(list.resultData.isEmpty()){
                    Box(Modifier.fillMaxSize()){
                        MFText(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.no_data))
                    }
                    return@Column
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(list.resultData.filterNotNull()){ item ->
                        ReceiveListItem(
                            navigateToMovieDetail = navigateToMovieDetail,
                            confirmRequest = { viewModel.confirmRequest(it) },
                            denyRequest = { viewModel.denyRequest(it) },
                            item = item
                        )
                    }
                }
            }
            else -> ReceiveListShimmer()
        }
    }
}

@Composable
fun ReceiveListShimmer(){
    Column {
        repeat(3){
            ShimmerEffect(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp))
        }
    }
}