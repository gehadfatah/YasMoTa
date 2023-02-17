package com.goda.movieapp.view.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.goda.movieapp.data.Resource
import com.goda.movieapp.data.Status
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.domain.repository.MovieRepository
import com.goda.movieapp.view.base.BaseViewModel
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repository: MovieRepository) :
    BaseViewModel() {


    fun getMovieFavorites(): LiveData<Resource<List<MovieResult>>> {
        progressState.postValue(true)
        return Transformations.map(repository.allFavoriteMovie()) {
            progressState.postValue(false)
            Resource(Status.SUCCESS, it, null)
        }
    }

    fun getProgressState(): LiveData<Boolean> = progressState

}