package org.comon.moviefriends.presenter.viewmodel.uistate

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PostUiState(
    var postCategory: MutableState<String> = mutableStateOf(""),
    var postTitle: MutableState<String> = mutableStateOf(""),
    var postContent: MutableState<String> = mutableStateOf(""),
    var postImageUrI: MutableState<String> = mutableStateOf(""),
)
