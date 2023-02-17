package com.goda.movieapp.view.ui.detail

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.goda.movieapp.R
import com.goda.movieapp.data.Resource
import com.goda.movieapp.domain.pagination.PaginationState
import com.goda.movieapp.domain.pojo.MovieDetail
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.view.customview.OverlapLoadingView
import com.goda.movieapp.view.ui.detail.adapter.ActorListAdapter
import com.goda.movieapp.util.Constants
import com.goda.movieapp.util.setEventStateValue
import com.goda.movieapp.util.showShortToast
import com.goda.movieapp.view.customview.EmptyView
import com.goda.movieapp.view.ui.home.SELL_BACK_KEY
import com.goda.movieapp.view.ui.home.SELL_BACK_VALUE
import com.goda.movieapp.view.ui.home.adapter.MovieReviewPagedListAdapter
import com.goda.movieapp.view.ui.home.adapter.OnRetryReview
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject


class DetailFragment : Fragment(R.layout.fragment_detail), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener, OnRetryReview {
    var rating = 0f

    private var snackbar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var pagedAdapter: MovieReviewPagedListAdapter

    private lateinit var viewModel: DetailViewModel
    private lateinit var actorsAdapter: ActorListAdapter

    private var movieResult: MovieResult? = null

    private var movieIsFavorite: Boolean = false

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        movieResult = requireArguments().getParcelable("movie")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            setEventStateValue(SELL_BACK_KEY, SELL_BACK_VALUE)
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        hideHideMovieOptionIncaseNotFromHome()

    }

    private fun hideHideMovieOptionIncaseNotFromHome() {

        ivHide.isVisible = when (findNavController().previousBackStackEntry?.destination?.id) {
            R.id.navigation_home -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateCachedUI()
        setListener()
        obsState()
        obsData()
        initView()
        movieResult?.id?.let { viewModel.loadMovieDetail(it) }
        movieResult?.id?.let { viewModel.getReviews(it) }
    }

    private fun setListener() {

        ratingBarReviews.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            Log.d("jf", rating.toString())
            if (rating == 0f) return@setOnRatingChangeListener
            this.rating = rating
            Handler().postDelayed(Runnable {
                sendReviewUser()
            }, 500)
        }
    }

    private fun sendReviewUser() {
        activity?.showShortToast(rating.toString())

    }

    private fun initView() {
        emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
        swipe.setOnRefreshListener(this)
        pagedAdapter = MovieReviewPagedListAdapter(this)
        popularList.layoutManager = LinearLayoutManager(activity)
        popularList.adapter = pagedAdapter
    }


    private fun obsData() {
        viewModel.getMovieDetail().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
        viewModel.movieIsFavorite(movieResult?.id.toString())
            .observe(viewLifecycleOwner, Observer {
                changeFavoriteIcon(it.isNotEmpty() && it.first().isFavourite)
            })
        viewModel.movieIsHide(movieResult?.id.toString())
            .observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty() && it.first().isHide) context?.showShortToast("You not see this movie again :)")
            })
        viewModel.moviePagedLiveData.observe(viewLifecycleOwner, Observer { pagedList ->
            pagedAdapter.submitList(pagedList)
        })
    }

    private fun changeFavoriteIcon(isFavorite: Boolean) {
        movieIsFavorite = isFavorite
        ivFavorite.setImageResource(if (isFavorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp)
    }

    private fun updateUI(resource: Resource<MovieDetail>?) {
        if (resource?.data != null) {
            if (resource.data.genres != null) {
                val stringCommaGnre = resource.data.genres.joinToString { it.name }
                tvGnreValue.text = stringCommaGnre
            }
            movie_release_date.text = resource.data.releaseDate
            movie_rating.text = resource.data.voteCount.toString()
            movie_popularity.text = resource.data.popularity.toString()
            movie_releasedate.text = resource.data.releaseDate
            if (resource.data.runtime != null) {
                val hours = resource.data.runtime / 60
                val min = resource.data.runtime % 60
                tvDuration.text = String.format("%sh %smin", hours, min)
                tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_schedule_black_24dp,
                    0, 0, 0
                )
                tvDuration.compoundDrawablePadding = 5
            }
            if (!resource.data.overview.isNullOrEmpty()) {
                tvDescriptionValue.visibility = View.VISIBLE
                tvDescriptionTitle.visibility = View.VISIBLE
                tvDescriptionValue.text = resource.data.overview
            } else {
                tvDescriptionValue.visibility = View.GONE
                tvDescriptionTitle.visibility = View.GONE
            }
            actorsAdapter.submitList(resource.data.credits?.cast ?: listOf())
            loadingView.loadingStateType(OverlapLoadingView.STATETYPE.DONE)
            contentDescription.visibility = View.VISIBLE
        } else {
            loadingView.loadingStateType(OverlapLoadingView.STATETYPE.ERROR)
            snackbar =
                Snackbar.make(
                    nestedDetail,
                    getString(R.string.no_internet_connection),
                    Snackbar.LENGTH_INDEFINITE
                )
            snackbar?.setAction("Retry") {
                snackbar?.dismiss()
                Handler().postDelayed({
                    movieResult?.id?.let { it1 -> viewModel.loadMovieDetail(it1) }
                    viewModel.movieId = movieResult?.id ?: 0
                }, 250)
            }
            snackbar?.show()
        }
    }

    private fun obsState() {
        viewModel.getProgresState().observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                loadingView.loadingStateType(OverlapLoadingView.STATETYPE.LOADING)
            }
        })

        viewModel.paginationState?.observe(viewLifecycleOwner, Observer {
            updateUIPaginationState(it)
            pagedAdapter.updatePaginationState(it)
        })
    }

    private fun updateCachedUI() {
        ivFavorite.setOnClickListener(this)
        ivHide.setOnClickListener(this)
        Picasso.get().load(Constants.BASE_IMAGE_URL_w500_API + movieResult?.backdrop_path)
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.loading_animation)
            .into(ivBackdrop)
        Picasso.get().load(Constants.BASE_IMAGE_URL_API + movieResult?.poster_path)
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.loading_animation)
            .into(ivPoster)
        tvMovieTitleValue.text = movieResult?.title
        if ((movieResult?.title?.length ?: 0) > 10) {
            tvMovieTitleValue.isSelected = true
        }
        tvVoteAverage.text = movieResult?.vote_average.toString()
        actorsAdapter = ActorListAdapter()
        actors.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        actors.adapter = actorsAdapter
    }

    override fun onDestroyView() {
        if (snackbar != null && snackbar?.isShown == true)
            snackbar?.dismiss()
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivFavorite -> {
                movieResult?.let { viewModel.saveFavorite(it, !movieIsFavorite) }
            }
            R.id.ivHide -> {
                movieResult?.let { viewModel.saveHideMovie(it, true) }
            }
        }
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
                    emptyView.emptyStateType(
                        EmptyView.STATETYPE.CONNECTION,
                        View.OnClickListener { onRefresh() })
                }
            }
            PaginationState.DONE -> {
                swipe.isRefreshing = false
                emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
            }
            else -> {}
        }
    }

    override fun onClickRetry() {
        viewModel.refreshFailedRequest()
    }

    override fun onRefresh() {
        viewModel.refreshAllList()
    }
}
