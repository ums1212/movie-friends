package org.comon.moviefriends.common

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
    HOUR(24,30,"일 전"),
    DAY(30,12,"달 전"),
    MONTH(12,Int.MAX_VALUE,"년 전")
}


