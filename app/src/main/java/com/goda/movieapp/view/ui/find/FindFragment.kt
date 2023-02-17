package com.goda.movieapp.view.ui.find

import android.animation.LayoutTransition
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
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
import com.goda.movieapp.view.customview.EmptyView
import com.goda.movieapp.view.ui.home.adapter.MoviePagedListAdapter
import com.goda.movieapp.view.ui.home.adapter.MovieInteractionListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_find.emptyView
import kotlinx.android.synthetic.main.fragment_find.searchList
import kotlinx.android.synthetic.main.fragment_find.swipe
import kotlinx.android.synthetic.main.search_layout.*
import javax.inject.Inject


class FindFragment : Fragment(R.layout.search_layout), SwipeRefreshLayout.OnRefreshListener,
    MovieInteractionListener {
       var searchText:String=""
    private lateinit var searchView: SearchView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var findViewModel: FindViewModel

    private lateinit var pagedAdapter: MoviePagedListAdapter


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        findViewModel =
            ViewModelProvider(this, viewModelFactory).get(FindViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
        obsStates()
        obsData()
    }

    private fun initUI() {
        emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
        swipe.setOnRefreshListener(this)
        pagedAdapter = MoviePagedListAdapter(this)
        searchList.layoutManager = gridLayoutManager()
        searchList.adapter = pagedAdapter
    }

    private fun obsData() {
        findViewModel.moviePagedLiveData.observe(viewLifecycleOwner, Observer { pagedList ->
            pagedAdapter.submitList(pagedList)
        })
    }

    private fun obsStates() {
        findViewModel.paginationState?.observe(viewLifecycleOwner, Observer {
            updateUIPaginationState(it)
            pagedAdapter.updatePaginationState(it)
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

    private fun updateUIPaginationState(paginationState: PaginationState?) {
        when (paginationState) {
            PaginationState.LOADING -> {
                swipe.isRefreshing = true
            }
            (PaginationState.EMPTY )-> {
                swipe.isRefreshing = false
                if (pagedAdapter.currentList.isNullOrEmpty()&&searchText.isNotEmpty()) {
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
        findViewModel.refreshAllList()
    }

    override fun onClickRetry() {
        findViewModel.refreshFailedRequest()
    }

    override fun onMovieClick(movieResult: MovieResult, pos: Int) {
        val bundle = bundleOf("movie" to movieResult)
        findNavController().navigate(R.id.detail_fragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

       /* val menuItemSearch = menu.findItem(R.id.action_search)
        searchView = menuItemSearch.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        menuItemSearch.expandActionView()
        personalizeSearchView()*/
        initViewListener()
    }

    private fun initViewListener() {
      /*  searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                this.callSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                this.callSearch(newText)
                return false
            }

            private fun callSearch(query: String) {
                searchText=query
                findViewModel.searchMovieByName(query)
            }
        })*/
        edtMoviesName.doOnTextChanged {
                str, _, _, _ ->findViewModel.searchMovieByName(str.toString())
        }
        buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        back.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }

    private fun personalizeSearchView(){
        val txtSearch = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        txtSearch.hint = getString(R.string.search_hint)
        txtSearch.setHintTextColor(ContextCompat.getColor(requireContext(),R.color.White_40))
        txtSearch.setTextColor(ContextCompat.getColor(requireContext(), R.color.White_100))

        // change search close button image
        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)
        closeButton.setImageResource(R.drawable.ic_close_black_24dp)

        // Make animation transition
        val searchBar = searchView.findViewById(R.id.search_bar) as LinearLayout
        searchBar.layoutTransition = LayoutTransition()
    }

}
