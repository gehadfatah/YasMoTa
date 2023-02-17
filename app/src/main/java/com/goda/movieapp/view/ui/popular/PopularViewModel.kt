package com.goda.movieapp.view.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.goda.movieapp.domain.pagination.MovieDataSourceFactory
import com.goda.movieapp.domain.pagination.PaginationState
import com.goda.movieapp.domain.repository.MovieRepository
import com.goda.movieapp.util.QueryHelper
import com.goda.movieapp.view.base.BaseViewModel
import javax.inject.Inject

class PopularViewModel @Inject constructor(repository: MovieRepository) : BaseViewModel()  {

    private var movieDataSourceFactory: MovieDataSourceFactory =
        MovieDataSourceFactory(
            MovieRepository.QUERYTAG.POPULAR,
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
}
