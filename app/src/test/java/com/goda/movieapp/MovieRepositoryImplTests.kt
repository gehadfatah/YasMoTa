package com.goda.movieapp

import com.goda.movieapp.TestMoviesCache
import com.goda.movieapp.TestsUtils
import com.goda.movieapp.data.local.MovieDao
import com.goda.movieapp.data.remote.Api
import com.goda.movieapp.domain.repository.MovieRepository
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class MovieRepositoryImplTests {


    private lateinit var api: Api
    private lateinit var movieCache: TestMoviesCache
    private lateinit var movieRepository: MovieRepository
    @Mock
    lateinit var dao: MovieDao
    @Before
    fun before() {
        api = mock(Api::class.java)
        movieCache = TestMoviesCache()
        movieRepository = MovieRepository(api, dao)
    }

  /*  @Test
    fun testWhenCacheIsNotEmptyGetMoviesReturnsCachedMovies() {

        movieCache.saveAll(generateMovieEntityList())
        movieRepository.allMovies().test()
                .assertComplete()
                .assertValue { movies -> movies.size == 5 }

        verifyZeroInteractions(api)
    }

    @Test
    fun testWhenCacheIsEmptyGetMoviesReturnsMoviesFromApi() {
        val movieListResult = MovieListResult()
        movieListResult.movies = TestsUtils.generateMovieDataList()
        `when`(api.getPopularMovies()).thenReturn(Observable.just(movieListResult))
        movieRepository.getMovies().test()
                .assertComplete()
                .assertValue { movies -> movies.size == 5 }
    }


    @Test
    fun testGetMovieByIdReturnedApiMovie() {
        val detailsData = TestsUtils.generateDetailsData(100)

        `when`(api.movieDetail(100)).thenReturn(Observable.just(detailsData))
        movieRepository.getMovie(100).test()
                .assertComplete()
                .assertValue { it.hasValue() && it.value == detailsDataMapper.mapFrom(detailsData) }
    }*/
}