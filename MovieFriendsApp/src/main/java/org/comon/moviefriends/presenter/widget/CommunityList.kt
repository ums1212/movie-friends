package org.comon.moviefriends.presenter.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.data.model.PostInfo

@Composable
//fun CommunityList(list: StateFlow<List<PostInfo>>, onNavigateToPostDetail: (postId: Int) -> Unit) {
fun CommunityList(onNavigateToPostDetail: (postId: Int) -> Unit) {

    val isLoading = remember { mutableStateOf(true) }
    val mutableList = remember { listOf<PostInfo>() }
    val stateLists = remember { mutableStateOf(mutableList) }

    LaunchedEffect(isLoading.value){
//        list.collectLatest {
//            stateLists.value = it
//        }
    }

    LazyColumn (
        Modifier.fillMaxHeight()
    ) {
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