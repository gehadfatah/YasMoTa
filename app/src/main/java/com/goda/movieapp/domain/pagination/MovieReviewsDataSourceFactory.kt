package com.goda.movieapp.domain.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.domain.pojo.ResultReviews
import com.goda.movieapp.domain.repository.MovieRepository

class MovieReviewsDataSourceFactory(
    private var movieId:Int,
    private var queryParams: HashMap<String, String>,
    private val repository: MovieRepository
) : DataSource.Factory<Int, ResultReviews>() {

    val movieDataSourceLiveData = MutableLiveData<MovieReviewDataSource>()

    override fun create(): DataSource<Int, ResultReviews> {
        val movieDataSource = MovieReviewDataSource(movieId, queryParams, repository)
        movieDataSourceLiveData.postValue(movieDataSource)
        return movieDataSource
    }

    fun getSource() = movieDataSourceLiveData.value

    fun getLastQueryParams() = queryParams
    fun getMovieId() = movieId


    fun updateQueryParams(movieId: Int,queryParams: HashMap<String, String>) {
        this.queryParams = queryParams
        this.movieId = movieId
        movieDataSourceLiveData.value?.refresh()
    }
}