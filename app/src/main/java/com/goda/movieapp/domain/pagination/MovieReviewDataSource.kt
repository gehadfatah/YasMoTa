package com.goda.movieapp.domain.pagination

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PageKeyedDataSource
import com.goda.movieapp.domain.pojo.ResultReviews
import com.goda.movieapp.domain.repository.MovieRepository
import com.goda.movieapp.util.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieReviewDataSource(
    private val movieId:Int,
    private val queryParams: HashMap<String, String>,
    private val repository: MovieRepository
) : PageKeyedDataSource<Int, ResultReviews>() {

    var state: SingleLiveEvent<PaginationState> = SingleLiveEvent()
    private var job = SupervisorJob()
    private val io: CoroutineContext = Dispatchers.IO
    private val scope = CoroutineScope(getJobErrorHandler() + io + job)
    private var retryQuery: (() -> Any)? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ResultReviews>
    ) {
        retryQuery = { loadInitial(params, callback) }
        executeQuery(1) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ResultReviews>) {
        val page = params.key
        retryQuery = { loadAfter(params, callback) }
        executeQuery(page ) {
            callback.onResult(it, page.plus(1))
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ResultReviews>) {
        //do nothing
    }

    private fun executeQuery(
        page: Int,
        callback: (List<ResultReviews>) -> Unit
    ) {
        updateState(PaginationState.LOADING)
        scope.launch {
            queryParams["page"] = page.toString()
            val result = repository.fetchReviewMovies(queryParams, movieId)
            retryQuery = null
            val listing = result.body()
            if (listing?.results != null && listing.results.isNotEmpty()) {
                updateState(PaginationState.DONE)
            } else updateState(PaginationState.EMPTY)
            callback(listing?.results ?: listOf())
        }
    }


    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e(MovieReviewDataSource::class.java.simpleName, "An error happened: $e")
        updateState(PaginationState.ERROR)
    }

    private fun updateState(pState: PaginationState) {
        this.state.postValue(pState)
    }

    fun refresh() =
        this.invalidate()

    override fun invalidate() {
        job.cancelChildren()
        super.invalidate()
    }

    fun retryFailedQuery() {
        val prevQuery = retryQuery
        retryQuery = null
        prevQuery?.invoke()
    }

    fun getPaginationState(): LiveData<PaginationState> =
        state

}