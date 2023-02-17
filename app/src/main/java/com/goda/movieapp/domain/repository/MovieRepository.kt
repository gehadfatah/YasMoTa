package com.goda.movieapp.domain.repository

import androidx.lifecycle.LiveData
import com.goda.movieapp.data.local.MovieDao
import com.goda.movieapp.data.remote.Api
import com.goda.movieapp.domain.pojo.MovieDetail
import com.goda.movieapp.domain.pojo.MovieQuery
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.domain.pojo.Review
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(private val api: Api, private val movieDao: MovieDao) {

    suspend fun fetchMovies(map: Map<String, String>, queryTag: QUERYTAG): Response<MovieQuery> {
       return when(queryTag){
            QUERYTAG.SEARCH -> api.searchMovie(map)
            QUERYTAG.POPULAR -> api.popularMovie(map)
            QUERYTAG.DISCOVER -> api.discoverMovie(map)
            QUERYTAG.TOPRATED -> api.topMovie(map)
            QUERYTAG.TRENDING -> api.trendingMovie(map["time_window"] ?: error("week"), map)
        }
    }
   suspend fun fetchReviewMovies(map: Map<String, String>,moviesId: Int): Response<Review> {
       return api.getReviewsMovie(moviesId,map)
   }

    suspend fun movieDetail(movieId: Long, query: String): Response<MovieDetail> {
        return api.movieDetail(movieId, query)
    }

    fun allFavoriteMovie(): LiveData<List<MovieResult>> {
        return movieDao.allFavorite()
    }
    suspend fun allHideable(): List<MovieResult> {
        return movieDao.allHideable()
    }
    fun allMovies(): LiveData<List<MovieResult>> {
        return movieDao.getAllMovies()
    }
    suspend fun insertAll(list: List<MovieResult>) {
        return movieDao.insertAllMovies(list)

    }
    suspend fun insert(movieResult: MovieResult) {
        return movieDao.insertFavorite(movieResult)
    }

    suspend fun update(movieResult: MovieResult) {
        return movieDao.updateFavorite(movieResult)
    }
    suspend fun updateHide(movieResult: MovieResult) {
        return movieDao.updateHide(movieResult)
    }
    suspend fun delete(movieResult: MovieResult) {
        return movieDao.deleteFavorite(movieResult)
    }

    fun existAsFavorite(id: String): LiveData<List<MovieResult>> {
        return movieDao.existAsFavorite(id)
    }
    fun existAsHide(id: String): LiveData<List<MovieResult>> {
        return movieDao.existAsHide(id)
    }

    enum class QUERYTAG {
        SEARCH, POPULAR, TRENDING,TOPRATED,DISCOVER
    }

}