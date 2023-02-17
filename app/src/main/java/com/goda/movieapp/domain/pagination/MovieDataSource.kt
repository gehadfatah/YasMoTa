package com.goda.movieapp.domain.pagination

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PageKeyedDataSource
import com.goda.movieapp.data.Resource
import com.goda.movieapp.data.Status
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.domain.repository.MovieRepository
import com.goda.movieapp.util.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieDataSource(
    private val queryTag: MovieRepository.QUERYTAG,
    private val queryParams: HashMap<String, String>,
    private val repository: MovieRepository
) : PageKeyedDataSource<Int, MovieResult>() {

    var state: SingleLiveEvent<PaginationState> = SingleLiveEvent()
    private var job = SupervisorJob()
    private val io: CoroutineContext = Dispatchers.IO
    private val scope = CoroutineScope(getJobErrorHandler() + io + job)
    private var retryQuery: (() -> Any)? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieResult>
    ) {
        retryQuery = { loadInitial(params, callback) }
        executeQuery(1, queryTag) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {
        val page = params.key
        retryQuery = { loadAfter(params, callback) }
        executeQuery(page, queryTag) {
            callback.onResult(it, page.plus(1))
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {
        //do nothing
    }

    private fun executeQuery(
        page: Int,
        queryTag: MovieRepository.QUERYTAG,
        callback: (List<MovieResult>) -> Unit
    ) {
        updateState(PaginationState.LOADING)
        scope.launch {
            queryParams["page"] = page.toString()
            val result = repository.fetchMovies(queryParams, queryTag)
            retryQuery = null
            val listing = result.body()
            val hideList = repository.allHideable()

            val newList = arrayListOf<MovieResult>()

            for (fj in listing?.results ?: emptyList()) {
                if (fj.id !in hideList.map { movieResult -> movieResult.id }) newList.add(fj)
            }
            listing?.results = newList
            if (listing?.results != null && listing?.results?.isNotEmpty() == true) {
                updateState(PaginationState.DONE)
            } else updateState(PaginationState.EMPTY)
            callback(listing?.results ?: listOf())
        }
    }


    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e(MovieDataSource::class.java.simpleName, "An error happened: $e")
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