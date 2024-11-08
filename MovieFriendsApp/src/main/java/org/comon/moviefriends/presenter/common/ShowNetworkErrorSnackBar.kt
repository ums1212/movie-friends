package org.comon.moviefriends.presenter.common

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.comon.moviefriends.R

@Composable
fun ShowNetworkErrorSnackBar(context: Context, scope: CoroutineScope){
    val snackBarHost = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            snackBarHost.showSnackbar(
                context.getString(R.string.network_error),
                null,
                true,
                SnackbarDuration.Short
            )
        }
    }
}