package com.goda.movieapp.data.remote

import com.goda.movieapp.domain.pojo.Review
import com.goda.movieapp.domain.pojo.MovieDetail
import com.goda.movieapp.domain.pojo.MovieQuery
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {
    @GET("movie/top_rated")
    suspend fun topMovie(@QueryMap params: Map<String, String>): Response<MovieQuery>

    @GET("discover/movie")
    suspend fun discoverMovie(@QueryMap params: Map<String, String>): Response<MovieQuery>

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviewsMovie(@Path("movie_id") id: Int,@QueryMap params: Map<String, String>):Response<Review>
    @GET("movie/popular")
    suspend fun popularMovie(@QueryMap params: Map<String, String>): Response<MovieQuery>

    @GET("trending/movie/{time_window}")
    suspend fun trendingMovie(@Path("time_window") time: String, @QueryMap params: Map<String, String>): Response<MovieQuery>

    @GET("search/movie")
    suspend fun searchMovie(@QueryMap params: Map<String, String>): Response<MovieQuery>

    @GET("movie/{movie_id}")
    suspend fun movieDetail(@Path("movie_id") movieId: Long, @Query("append_to_response") value: String): Response<MovieDetail>

}