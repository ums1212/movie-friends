package org.comon.moviefriends.data.datasource.lbs

import com.google.gson.GsonBuilder
import org.comon.moviefriends.data.model.tmdb.ResponseRegionDto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MFLocationService {

    @GET("v2/local/geo/coord2regioncode")
    suspend fun getCurrentRegion(
        @Query("x") longitude:Double,
        @Query("y") latitude:Double,
    ): Response<ResponseRegionDto>

    companion object {
        private var locationService: MFLocationService? = null
        fun getInstance() : MFLocationService {
            if (locationService == null) {
                val gson = GsonBuilder().serializeNulls().create()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_LOCATION_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(MFLocationOkHttpClient.getClient())
                    .build()
                locationService = retrofit.create(MFLocationService::class.java)
            }
            return locationService!!
        }
    }
}