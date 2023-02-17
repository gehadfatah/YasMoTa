package com.goda.movieapp

import com.goda.movieapp.domain.pojo.MovieDetail
import com.goda.movieapp.domain.pojo.MovieResult


class TestsUtils {

    companion object {
        fun getMockedMovieData(id: Int, title: String = "MovieData"): MovieDetail {
            return MovieDetail(
                    id = id,
                    title = title,
                    backdropPath = "movieData_backdrop",
                    posterPath = "movieData_poster",
                    popularity = 10.0,

            )
        }

        fun generateMovieDataList(): List<MovieResult> {

            return (0..4).map {
                MovieResult(
                        id = it.toLong(),
                        title = "Movie$it"
                )
            }
        }

        fun generateDetailsData(id: Int): MovieDetail {
            return MovieDetail(
                    id = id,
                    title = "Details Data",
                    backdropPath = "detailsData_backdrop",
                    posterPath = "detailsData_poster",
                    releaseDate = "1984-10-27",
                    popularity = 2.5,
                    voteAverage = 4.0,
                    voteCount = 102,
                    overview = "Overview"
            )
        }
    }

}