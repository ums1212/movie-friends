package org.comon.moviefriends.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.R
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.presentation.viewmodel.CommunityPostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityList(
    paddingValues: PaddingValues? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateToPostDetail: (postId: String) -> Unit
) {

    val viewModel: CommunityPostViewModel = hiltViewModel()

    val getAllPostState by viewModel.getAllPostState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit){
        viewModel.getALLPost()
    }

    /** 커뮤니티 메뉴일 때 */
    val modifier = if(scrollBehavior != null) {
        Modifier.fillMaxSize()
            .padding(paddingValues?: PaddingValues(0.dp))
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    }
    /** 프로필 메뉴일 때 */
    else{
        Modifier.fillMaxSize()
            .padding(paddingValues?: PaddingValues(0.dp))
    }
    LazyColumn (modifier = modifier) {
        when(val state = getAllPostState){
            APIResult.Loading -> {
                item {
                    CommunityListShimmer()
                }
            }
            is APIResult.NetworkError -> {
                item {
                    Spacer(Modifier.padding(vertical = 6.dp))
                    MFText(
                        textStyle = TextStyle(fontSize = 18.sp),
                        text = stringResource(R.string.network_error),
                    )
                }
            }
            is APIResult.Success -> {
                if(state.resultData.isEmpty()){
                    item {
                        Spacer(Modifier.padding(vertical = 6.dp))
                        MFText(
                            textStyle = TextStyle(fontSize = 18.sp),
                            text = stringResource(R.string.no_post),
                        )
                    }
                }else{
                    items(state.resultData) { item ->
                        if(item != null){
                            CommunityListItem(
                                post = item,
                                onNavigateToPostDetail = { onNavigateToPostDetail(item.id) }
                            )
                            Spacer(modifier = Modifier.padding(vertical = 6.dp))
                        }
                    }
                }
            }
            else -> {
                item {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun CommunityListShimmer(){
    ShimmerEffect(
        Modifier
            .fillMaxWidth()
            .height(180.dp))
    Spacer(Modifier.padding(vertical = 8.dp))
    ShimmerEffect(
        Modifier
            .fillMaxWidth()
            .height(180.dp))
    Spacer(Modifier.padding(vertical = 8.dp))
    ShimmerEffect(
        Modifier
            .fillMaxWidth()
            .height(180.dp))
    Spacer(Modifier.padding(vertical = 8.dp))
}