package com.goda.movieapp

import com.goda.movieapp.data.remote.Api
import com.goda.movieapp.domain.pojo.MovieResult

import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class RemoteMoviesDataStoreTests {
  /*  private val movieDataMovieEntityMapper = MovieDataEntityMapper()
    private val detailsDataMovieEntityMapper = DetailsDataMovieEntityMapper()
    private lateinit var api: Api
    private lateinit var remoteMoviesDataStore: RemoteMoviesDataStore
    @Before
    fun before() {
        api = Mockito.mock(Api::class.java)
        remoteMoviesDataStore = RemoteMoviesDataStore(
                api
                , movieDataMovieEntityMapper
                , detailsDataMovieEntityMapper)

    }

    @Test
    fun whenRequestingPopularMovieListFromRemoteSourceReturnExpectedResult() {
        val movies = (0..5).map {
            MovieResult(
                    id = it.toLong(),
                    title = "Movie$it",

            )
        }
        val movieListResult = MovieListResult()
        movieListResult.movies = movies
        movieListResult.page = 1
        Mockito.`when`(api.getPopularMovies()).thenReturn(Observable.just(movieListResult))
        remoteMoviesDataStore.getMovies().test()
                .assertValue { list -> list.size == 6 && list[0].title == "Movie0" }
                .assertComplete()
    }*/

}