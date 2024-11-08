package org.comon.moviefriends.presenter.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.data.model.PostInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityList(
    paddingValues: PaddingValues? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateToPostDetail: (postId: Int) -> Unit
) {

    val isLoading = remember { mutableStateOf(true) }
    val mutableList = remember { listOf<PostInfo>() }
    val stateLists = remember { mutableStateOf(mutableList) }

    LaunchedEffect(isLoading.value){
//        list.collectLatest {
//            stateLists.value = it
//        }
    }

    val modifier = if(scrollBehavior != null) {
        Modifier.fillMaxSize()
            .padding(paddingValues?: PaddingValues(0.dp))
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    }else{
        Modifier.fillMaxSize()
            .padding(paddingValues?: PaddingValues(0.dp))
    }

    LazyColumn (modifier = modifier) {
//        items(stateLists.value) { item ->
        items(listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)) { item ->
            CommunityListItem {
//                onNavigateToPostDetail(item.id)
                onNavigateToPostDetail(0)
            }
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
        }
    }

}