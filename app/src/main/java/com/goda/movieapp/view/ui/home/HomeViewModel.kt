package com.goda.movieapp.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.goda.movieapp.data.Resource
import com.goda.movieapp.data.Status
import com.goda.movieapp.domain.pagination.MovieDataSourceFactory
import com.goda.movieapp.domain.pagination.PaginationState
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.domain.repository.MovieRepository
import com.goda.movieapp.util.QueryHelper
import com.goda.movieapp.view.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: MovieRepository) : BaseViewModel() {

    private var movieDataSourceFactory: MovieDataSourceFactory =
        MovieDataSourceFactory(
            MovieRepository.QUERYTAG.DISCOVER,
            QueryHelper.popularMovies(),
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

        fun getMoviesFromDatabase(): LiveData<Resource<List<MovieResult>>> {
           progressState.postValue(true)
           return Transformations.map(repository.allMovies()) {
               progressState.postValue(false)
               Resource(Status.SUCCESS, it, null)
           }
       }
    fun insertAll(list: List<MovieResult>) {
        GlobalScope.launch(main + job) {
        repository.insertAll(list)
    }}

    fun getProgressState(): LiveData<Boolean> = progressState


}