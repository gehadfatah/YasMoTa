package com.goda.movieapp.view.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.goda.movieapp.R
import com.goda.movieapp.domain.pagination.PaginationState
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.util.goldEventChangedListener
import com.goda.movieapp.util.isNetworkConnected
import com.goda.movieapp.view.customview.EmptyView
import com.goda.movieapp.view.ui.favorite.adapter.FavoriteListAdapter
import com.goda.movieapp.view.ui.home.adapter.MoviePagedListAdapter
import com.goda.movieapp.view.ui.home.adapter.MovieClickerListener
import com.goda.movieapp.view.ui.home.adapter.MovieInteractionListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.emptyView
import javax.inject.Inject
const val SELL_BACK_KEY = "SELL_BACK_KEY"
const val SELL_BACK_VALUE = "SELL_BACK_VALUE"

class HomeFragment : Fragment(R.layout.fragment_home), SwipeRefreshLayout.OnRefreshListener,
    MovieInteractionListener, MovieClickerListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var pagedAdapter: MoviePagedListAdapter
    private lateinit var pagedAdapter2: FavoriteListAdapter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
        obsStates()
        obsData()
        initListner()
    }

    private fun initListner() {
        //this listener to handle when user click back from review screen we should reset user selection
        goldEventChangedListener(SELL_BACK_KEY) {
            if (it == SELL_BACK_VALUE) {
             onRefresh()
            }
        }
    }

    private fun obsStates() {
        homeViewModel.paginationState?.observe(viewLifecycleOwner, Observer {
            if (::pagedAdapter.isInitialized) {
                pagedAdapter.updatePaginationState(it)
            }
            updateUIPaginationState(it)
        })
    }

    private fun initUI() {
        emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
        swipe.setOnRefreshListener(this)

        if (activity?.isNetworkConnected() == false) {
            pagedAdapter2 = FavoriteListAdapter(this)
            trendingList.layoutManager = GridLayoutManager(requireActivity(), 2)
            trendingList.adapter = pagedAdapter2

        } else {
            pagedAdapter = MoviePagedListAdapter(this)
            trendingList.layoutManager = gridLayoutManager()
            trendingList.adapter = pagedAdapter
        }
    }

    private fun obsData() {
        homeViewModel.moviePagedLiveData.observe(viewLifecycleOwner, Observer { pagedList ->
            if (::pagedAdapter.isInitialized)
                pagedAdapter.submitList(pagedList)
            if (pagedList.isNotEmpty())
                homeViewModel.insertAll(pagedList.snapshot())
            else homeViewModel.getMoviesFromDatabase()
                .observe(viewLifecycleOwner, Observer { favorites ->
                    if (favorites.data != null && favorites.data.isNotEmpty()) {
                        if (::pagedAdapter2.isInitialized){
                            swipe.isRefreshing = false
                            pagedAdapter2.submitList(favorites.data)
                        }
                    }

                })
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
        homeViewModel.refreshFailedRequest()
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
                if (::pagedAdapter.isInitialized&&pagedAdapter.currentList.isNullOrEmpty()) {
                    emptyView.emptyStateType(EmptyView.STATETYPE.EMPTY, null)
                }
            }
            PaginationState.ERROR -> {
                swipe.isRefreshing = false
                if (::pagedAdapter.isInitialized&&pagedAdapter.currentList.isNullOrEmpty()) {
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
        homeViewModel.refreshAllList()
    }
}
