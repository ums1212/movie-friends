package org.comon.moviefriends.data.datasource.tmdb

import org.comon.moviefriends.data.entity.tmdb.ResponseCreditDto
import org.comon.moviefriends.data.entity.tmdb.ResponseMovieDetailDto
import org.comon.moviefriends.data.entity.tmdb.ResponseMovieVideoDto
import org.comon.moviefriends.data.entity.tmdb.ResponseMoviesDto
import org.comon.moviefriends.data.entity.tmdb.ResponseSearchMovieDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {

    // 현재 상영작
    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("language") language:String = "ko-KR",
        @Query("region") region:String = "kr",
    ): Response<ResponseMoviesDto>

    // 인기작
    @GET("movie/popular")
    suspend fun getPopular(
        @Query("language") language:String = "ko-KR",
        @Query("region") region:String = "kr",
    ): Response<ResponseMoviesDto>

    // 트렌드
    @GET("trending/movie/{time_window}")
    suspend fun getTrending(
        @Path("time_window") timeWindow:String = "day",
        @Query("language") language:String = "ko-KR",
    ): Response<ResponseMoviesDto>

    // 개봉예정작
    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("language") language:String = "ko-KR",
        @Query("region") region:String = "kr",
    ): Response<ResponseMoviesDto>

    // 영화 상세정보
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId:Int,
        @Query("language") language:String = "ko-KR",
    ): Response<ResponseMovieDetailDto>

    // 영화 주요 출연진
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredit(
        @Path("movie_id") movieId:Int,
        @Query("language") language:String = "ko-KR",
    ): Response<ResponseCreditDto>

    // 영화 영상 정보
    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId:Int,
        @Query("language") language:String = "ko-KR",
    ): Response<ResponseMovieVideoDto>

    // 영화 검색
    @GET("search/movie")
    suspend fun searchMovieByTitle(
        @Query("query") movieTitle:String,
        @Query("language") language:String = "ko-KR",
        @Query("primary_release_year") releaseYear:String,
    ): Response<ResponseSearchMovieDto>
}