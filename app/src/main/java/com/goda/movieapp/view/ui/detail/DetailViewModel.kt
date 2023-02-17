package com.goda.movieapp.view.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import com.goda.movieapp.data.Resource
import com.goda.movieapp.data.Status
import com.goda.movieapp.domain.pagination.MovieReviewsDataSourceFactory
import com.goda.movieapp.domain.pagination.PaginationState
import com.goda.movieapp.domain.pojo.MovieDetail
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.domain.repository.MovieRepository
import com.goda.movieapp.util.SingleLiveEvent
import com.goda.movieapp.view.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val repository: MovieRepository) :
    BaseViewModel() {
    var movieId: Long=0
    private val movieDetail: MutableLiveData<Resource<MovieDetail>> = MutableLiveData()

    private val favoriteSavedState: SingleLiveEvent<Resource<Pair<Boolean, Boolean>>> =
        SingleLiveEvent()

    fun loadMovieDetail(movieId: Long) = viewModelScope.launch {
        progressState.postValue(true)
        try {
            val result = repository.movieDetail(movieId, "credits")
            val sublistResult = getFirstItemWithProfilePath(result)
            movieDetail.postValue(Resource(Status.SUCCESS, sublistResult, null))
        } catch (e: Exception) {
            movieDetail.postValue(Resource(Status.ERROR, null, e))
        }
        progressState.postValue(false)
    }

    fun getReviews(movieId: Long) {

        val querySearch = java.util.HashMap<String, String>()
        movieDataSourceFactory.updateQueryParams(movieId = movieId.toInt(),querySearch)

    }

    private suspend fun getFirstItemWithProfilePath(
        result: Response<MovieDetail>
    ): MovieDetail? = withContext(default + job) {
        if (result.isSuccessful && result.body() != null) {
            val actorCurrentList = result.body()?.credits?.cast ?: arrayListOf()
            if (actorCurrentList.isNotEmpty()) {
                val filteredList = actorCurrentList.filter {
                    it.profile_path != null
                }
                result.body()?.credits?.cast = filteredList
            }
            result.body()
        } else {
            null
        }
    }

    fun saveFavorite(movieLocal: MovieResult, favorite: Boolean) = viewModelScope.launch {
        try {
            movieLocal.isFavourite=favorite
           /* if (favorite) {
                repository.insert(movieLocal)
            } else {
                repository.delete(movieLocal)
            }*/
            repository.update(movieLocal)
            favoriteSavedState.postValue(Resource(Status.SUCCESS, Pair(true, favorite), null))
        } catch (e: Exception) {
            favoriteSavedState.postValue(Resource(Status.ERROR, null, e))
        }
    }
    fun saveHideMovie(movieLocal: MovieResult, hide: Boolean=true) =
        viewModelScope.launch {
        try {
            movieLocal.isHide=hide
            repository.updateHide(movieLocal)
            favoriteSavedState.postValue(Resource(Status.SUCCESS, Pair(true, hide), null))
        } catch (e: Exception) {
            favoriteSavedState.postValue(Resource(Status.ERROR, null, e))
        }
    }

    fun getMovieDetail(): LiveData<Resource<MovieDetail>> = movieDetail

    fun getProgresState(): LiveData<Boolean> = progressState

    fun getFavoriteSavedState(): SingleLiveEvent<Resource<Pair<Boolean, Boolean>>> = favoriteSavedState

    fun movieIsFavorite(id: String): LiveData<List<MovieResult>> = repository.existAsFavorite(id)
    fun movieIsHide(id: String): LiveData<List<MovieResult>> = repository.existAsHide(id)




    private var movieDataSourceFactory: MovieReviewsDataSourceFactory =
        MovieReviewsDataSourceFactory(
            movieId.toInt(),
            HashMap<String, String>(),
            repository
        )

    val moviePagedLiveData = pagedConfig.let {
        LivePagedListBuilder(movieDataSourceFactory,
            it
        ).build()
    }

    val paginationState: LiveData<PaginationState>? =
        Transformations.switchMap(movieDataSourceFactory.movieDataSourceLiveData) { it.getPaginationState() }

    /**
     * Retry possible last paged request (ie: network issue)
     */
    fun refreshFailedRequest() =
        movieDataSourceFactory.getSource()?.retryFailedQuery()

    /**
     * Refreshes all list after an issue
     */
    fun refreshAllList() =
        movieDataSourceFactory.getSource()?.refresh()


}
