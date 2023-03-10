package com.goda.movieapp.view.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.goda.movieapp.R
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.view.customview.EmptyView
import com.goda.movieapp.view.ui.favorite.adapter.FavoriteListAdapter
import com.goda.movieapp.view.ui.home.adapter.MovieInteractionListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_favorite.*
import javax.inject.Inject

class FavoriteFragment : Fragment(R.layout.fragment_favorite), MovieInteractionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var favoriteAdapter: FavoriteListAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
        obsStates()
        obsData()
    }

    private fun obsData() {
        favoriteViewModel.getMovieFavorites().observe(viewLifecycleOwner, Observer { favorites ->
            if (favorites.data != null && favorites.data.isNotEmpty()) {
                favoriteAdapter.submitList(favorites.data)
            } else {
                emptyView.emptyStateType(EmptyView.STATETYPE.EMPTY, null)
            }
        })
    }

    private fun obsStates() {
        favoriteViewModel.getProgressState().observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                favoriteLoading.visibility = View.VISIBLE
            } else favoriteLoading.visibility = View.GONE
        })
    }

    private fun initUI() {
        emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
        favoriteAdapter = FavoriteListAdapter(this)
        favorites.layoutManager = GridLayoutManager(requireActivity(), 2)
        favorites.adapter = favoriteAdapter
    }

    override fun onClickRetry() {
        //do nothing
    }

    override fun onMovieClick(movieResult: MovieResult, pos: Int) {
        findNavController().navigate(R.id.detail_fragment, bundleOf("movie" to movieResult))
    }


}
