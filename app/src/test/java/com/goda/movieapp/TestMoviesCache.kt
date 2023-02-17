package com.goda.movieapp


import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.util.MoviesCache
import com.goda.movieapp.util.Optional
import io.reactivex.Observable

class TestMoviesCache : MoviesCache {

    private val movies: HashMap<Int, MovieResult> = HashMap()


    override fun isEmpty(): Boolean {
        return movies.isEmpty()
    }

    override fun remove(movieEntity: MovieResult) {
        movies.remove(movieEntity.id.toInt())
    }

    override fun clear() {
        movies.clear()
    }

    override fun save(movieEntity: MovieResult) {
        movies[movieEntity.id.toInt()] = movieEntity
    }

    override fun saveAll(movieEntities: List<MovieResult>) {
        movieEntities.forEach { movieEntity -> this.movies[movieEntity.id.toInt()] = movieEntity }
    }

    override fun getAll(): Observable<List<MovieResult>> {
        return Observable.just(movies.values.toList())
    }

    override fun get(movieId: Int): Observable<Optional<MovieResult>> {
        return Observable.just(Optional.of(movies[movieId]))
    }
}

