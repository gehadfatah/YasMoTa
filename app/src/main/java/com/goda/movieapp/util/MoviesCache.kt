package com.goda.movieapp.util

import com.goda.movieapp.domain.pojo.MovieResult

import io.reactivex.Observable
import java.util.*

interface MoviesCache {

    fun clear()
    fun save(movieEntity: MovieResult)
    fun remove(movieEntity: MovieResult)
    fun saveAll(movieEntities: List<MovieResult>)
    fun getAll(): Observable<List<MovieResult>>
    fun get(movieId: Int): Observable<Optional<MovieResult>>
    fun isEmpty(): Boolean
}