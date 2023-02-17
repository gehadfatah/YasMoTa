package com.goda.movieapp.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.goda.movieapp.MovieGApp
import com.goda.movieapp.data.local.MovieDao
import com.goda.movieapp.data.local.MovieLocalStorage
import com.goda.movieapp.data.remote.Api
import com.goda.movieapp.domain.repository.MovieRepository
import com.goda.movieapp.util.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Provides
    internal fun provideContext(application: MovieGApp): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(api: Api, movieDao: MovieDao): MovieRepository {
        return MovieRepository(api, movieDao)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MovieLocalStorage = Room.databaseBuilder(
        context,
        MovieLocalStorage::class.java, Constants.DATABASE
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(
        database: MovieLocalStorage
    ): MovieDao = database.movieDao()

    @Provides
    @Singleton
    fun providePreference(
        context: Context
    ): SharedPreferences = context.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE)

}