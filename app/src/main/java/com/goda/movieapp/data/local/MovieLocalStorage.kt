package com.goda.movieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.goda.movieapp.domain.pojo.MovieResult

@Database(entities = [(MovieResult::class)], version = 2, exportSchema = false)
abstract class MovieLocalStorage : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}