package org.comon.moviefriends.common

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.comon.moviefriends.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showSnackBar(
    scope: CoroutineScope,
    snackBarHost: SnackbarHostState,
    localContext: Context,
    beforeSnackBar: ()->Unit = {},
    afterSnackBar: ()->Unit = {},
){
    scope.launch {
        beforeSnackBar()
        snackBarHost.showSnackbar(
            localContext.getString(R.string.network_error),
            null,
            true,
            SnackbarDuration.Short
        )
        afterSnackBar()
    }
}

fun <T> LifecycleOwner.collectFlowInActivity(flow: Flow<T>, action: suspend (value: T) -> Unit){
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(action)
        }
    }
}

fun getTimeDiff(time : Long): String {
    val curTime = Timestamp.now().seconds
    var diffTime = curTime - time
    var msg = "방금 전"
    if(diffTime >= TimeValue.SEC.value ) {
        for (i in TimeValue.entries) {
            diffTime /= i.value
            if (diffTime < i.maximum) {
                if(i==TimeValue.DATE_FORMAT){
                    return getDateString(time)
                }
                msg="$diffTime ${i.msg}"
                break
            }
        }
    }
    return msg
}

enum class TimeValue(val value: Int,val maximum : Int, val msg : String) {
    SEC(60,60,"분 전"),
    MIN(60,24,"시간 전"),
    DATE_FORMAT(24,Int.MAX_VALUE,""),
}

fun getDateString(seconds : Long): String {
    try {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = seconds * 1000
        return dateFormat.format(calendar.time)
    }catch (e: Exception){
        return ""
    }
}