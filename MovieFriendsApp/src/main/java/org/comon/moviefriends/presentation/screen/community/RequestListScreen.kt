package org.comon.moviefriends.presentation.screen.community

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
import org.comon.moviefriends.presentation.components.MFTitle
import org.comon.moviefriends.presentation.components.MFText
import org.comon.moviefriends.presentation.components.RequestListItem
import org.comon.moviefriends.presentation.components.ShimmerEffect
import org.comon.moviefriends.presentation.theme.FriendsBlack
import org.comon.moviefriends.presentation.viewmodel.WatchTogetherViewModel

@Composable
fun RequestListScreen(
    navigateToMovieDetail: (id:Int) -> Unit,
    watchTogetherViewModel: WatchTogetherViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = Unit) {
        watchTogetherViewModel.getUserInfo(MFPreferences.getUserInfo())
        watchTogetherViewModel.getMyRequestList()
    }

    val localContext = LocalContext.current
    val snackBarHost = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val requestList = watchTogetherViewModel.myChatRequestList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FriendsBlack),
    ) {
        MFTitle(text = stringResource(R.string.label_request_list), modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider()
        SnackbarHost(hostState = snackBarHost)
        when(val list = requestList.value){
            APIResult.Loading -> RequestListShimmer()
            is APIResult.NetworkError -> {
                showSnackBar(coroutineScope, snackBarHost, localContext)
                Box(Modifier.fillMaxSize()){
                    MFText(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.network_error))
                }
            }
            APIResult.NoConstructor -> RequestListShimmer()
            is APIResult.Success -> {
                if(list.resultData.isEmpty()){
                    Box(Modifier.fillMaxSize()){
                        MFText(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.no_data))
                    }
                }else{
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(list.resultData.filterNotNull()){ item ->
                            RequestListItem(
                                coroutineScope = coroutineScope,
                                snackBarHost = snackBarHost,
                                localContext = localContext,
                                navigateToMovieDetail = navigateToMovieDetail,
                                watchTogetherViewModel = watchTogetherViewModel,
                                item = item
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RequestListShimmer(){
    Column {
        repeat(3){
            ShimmerEffect(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp))
        }
    }
}