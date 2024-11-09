package org.comon.moviefriends.data.datasource.lbs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.datasource.tmdb.APIResult

class MFLocationManager {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest

    companion object {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }

    fun getCurrentLocation(context: Context) = flow {
        emit(APIResult.Loading)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mLocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10000L
        ).run {
            setWaitForAccurateLocation(true)
            setMinUpdateIntervalMillis(3000L)
            setIntervalMillis(3000L) //위치가 update 되는 주기
            setMaxUpdateDelayMillis(3000L)
                .build()
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            emit(APIResult.NetworkError(Exception("권한 승인 안됨")))
        }
        val result = mFusedLocationClient.getCurrentLocation(100, createCancellationToken()).await()
        val resultList = listOf(
            result.latitude,
            result.longitude
        )
        emit(APIResult.Success(resultList))
    }.catch {
        emit(APIResult.NetworkError(it))
    }

    private fun createCancellationToken(): CancellationToken
        = object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                return CancellationTokenSource().token
            }

            override fun isCancellationRequested(): Boolean {
                return false
            }

        }
}