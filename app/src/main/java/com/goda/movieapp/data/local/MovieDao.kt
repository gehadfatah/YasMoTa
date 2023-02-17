package com.goda.movieapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.goda.movieapp.domain.pojo.MovieResult

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllMovies(list: List<MovieResult>)

    @Query("SELECT * FROM MovieLocal")
    fun getAllMovies(): LiveData<List<MovieResult>>

    @Query("SELECT * FROM MovieLocal  WHERE isFavourite=:favourite")
    fun allFavorite(favourite: Boolean = true): LiveData<List<MovieResult>>

    @Query("SELECT * FROM MovieLocal  WHERE isHide=:hide")
    suspend fun allHideable(hide: Boolean = true): List<MovieResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movieLocal: MovieResult)

    @Update
    suspend fun updateFavorite(movieLocal: MovieResult)

    @Update
    suspend fun updateHide(movieLocal: MovieResult)

    @Delete
    suspend fun deleteFavorite(movieLocal: MovieResult)

    @Query("SELECT * FROM MovieLocal WHERE id=:id LIMIT 1")
    fun existAsFavorite(id: String): LiveData<List<MovieResult>>
    @Query("SELECT * FROM MovieLocal WHERE id=:id LIMIT 1")
    fun existAsHide(id: String): LiveData<List<MovieResult>>
}