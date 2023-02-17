package com.goda.movieapp.view.ui.child

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.goda.movieapp.R
import com.goda.movieapp.domain.pagination.PaginationState
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.view.customview.EmptyView
import com.goda.movieapp.view.ui.home.adapter.MoviePagedListAdapter
import com.goda.movieapp.view.ui.home.adapter.MovieInteractionListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_child.*
import javax.inject.Inject

class TopRatedFragment : Fragment(R.layout.fragment_child) , SwipeRefreshLayout.OnRefreshListener,
    MovieInteractionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var pagedAdapter: MoviePagedListAdapter

    private lateinit var topRatedViewModel: TopRatedViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topRatedViewModel = ViewModelProvider(this,viewModelFactory).get(TopRatedViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
        obsStates()
        obsData()
    }

    private fun obsStates() {
        topRatedViewModel.paginationState?.observe(viewLifecycleOwner, Observer {
            updateUIPaginationState(it)
            pagedAdapter.updatePaginationState(it)
        })
    }

    private fun initUI() {
        emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
        swipe.setOnRefreshListener(this)
        pagedAdapter = MoviePagedListAdapter(this)
        childList.layoutManager = gridLayoutManager()
        childList.adapter = pagedAdapter
    }

    private fun obsData() {
        topRatedViewModel.moviePagedLiveData.observe(viewLifecycleOwner, Observer { pagedList ->
            pagedAdapter.submitList(pagedList)
        })
    }

    private fun gridLayoutManager(): RecyclerView.LayoutManager? {
        val mLayoutManager = GridLayoutManager(activity, 2)
        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (pagedAdapter.getItemViewType(position)) {
                    R.layout.loading_view_layout -> mLayoutManager.spanCount
                    else -> 1
                }
            }
        }
        return mLayoutManager
    }

    override fun onClickRetry() {
        topRatedViewModel.refreshFailedRequest()
    }

    override fun onMovieClick(movieResult: MovieResult, pos: Int) {
        val bundle = bundleOf("movie" to movieResult)
        findNavController().navigate(R.id.detail_fragment, bundle)
    }

    private fun updateUIPaginationState(paginationState: PaginationState?) {
        when (paginationState) {
            PaginationState.LOADING -> {
                swipe.isRefreshing = true
            }
            PaginationState.EMPTY -> {
                swipe.isRefreshing = false
                if (pagedAdapter.currentList.isNullOrEmpty()) {
                    emptyView.emptyStateType(EmptyView.STATETYPE.EMPTY, null)
                }
            }
            PaginationState.ERROR -> {
                swipe.isRefreshing = false
                if (pagedAdapter.currentList.isNullOrEmpty()) {
                    emptyView.emptyStateType(EmptyView.STATETYPE.CONNECTION, View.OnClickListener {
                        onRefresh()
                    })
                }
            }
            PaginationState.DONE -> {
                swipe.isRefreshing = false
                emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
            }
            else -> {}
        }
    }

    override fun onRefresh() {
        topRatedViewModel.refreshAllList()
    }

}
