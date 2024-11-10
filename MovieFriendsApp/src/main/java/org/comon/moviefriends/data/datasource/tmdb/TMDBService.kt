package org.comon.moviefriends.data.datasource.tmdb

import com.google.gson.GsonBuilder
import org.comon.moviefriends.data.model.tmdb.ResponseCreditDto
import org.comon.moviefriends.data.model.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.model.tmdb.ResponseMovieVideoDto
import org.comon.moviefriends.data.model.tmdb.ResponseMoviesDto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {

    // 현재 상영작
    @GET("now_playing")
    suspend fun getNowPlaying(
        @Query("language") language:String = "ko-KR",
        @Query("region") region:String = "kr",
    ): Response<ResponseMoviesDto>

    // 인기작
    @GET("popular")
    suspend fun getPopular(
        @Query("language") language:String = "ko-KR",
        @Query("region") region:String = "kr",
    ): Response<ResponseMoviesDto>

    // 랭킹
    @GET("top_rated")
    suspend fun getTopRated(
        @Query("language") language:String = "ko-KR",
        @Query("region") region:String = "kr",
    ): Response<ResponseMoviesDto>

    // 개봉예정작
    @GET("upcoming")
    suspend fun getUpcoming(
        @Query("language") language:String = "ko-KR",
        @Query("region") region:String = "kr",
    ): Response<ResponseMoviesDto>

    // 영화 상세정보
    @GET("{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId:Int,
        @Query("language") language:String = "ko-KR",
    ): Response<ResponseMovieDetailDto>

    // 영화 주요 출연진
    @GET("{movie_id}/credits")
    suspend fun getMovieCredit(
        @Path("movie_id") movieId:Int,
        @Query("language") language:String = "ko-KR",
    ): Response<ResponseCreditDto>

    // 영화 영상 정보
    @GET("{movie_id}/videos")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId:Int,
        @Query("language") language:String = "ko-KR",
    ): Response<ResponseMovieVideoDto>

    companion object {
        private var tdmbService: TMDBService? = null
        fun getInstance() : TMDBService {
            if (tdmbService == null) {
                val gson = GsonBuilder().serializeNulls().create()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_TMDB_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(TMDBOkHttpClient.getClient())
                    .build()
                tdmbService = retrofit.create(TMDBService::class.java)
            }
            return tdmbService!!
        }
    }

}