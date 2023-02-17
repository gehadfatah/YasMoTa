package com.goda.movieapp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goda.movieapp.common.ViewModelFactory
import com.goda.movieapp.di.key.ViewModelKey
import com.goda.movieapp.view.ui.child.TopRatedViewModel
import com.goda.movieapp.view.ui.detail.DetailViewModel
import com.goda.movieapp.view.ui.favorite.FavoriteViewModel
import com.goda.movieapp.view.ui.find.FindViewModel
import com.goda.movieapp.view.ui.home.HomeViewModel
import com.goda.movieapp.view.ui.popular.PopularViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(homeViewModel: HomeViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    fun bindFavoriteViewModel(favoriteViewModel: FavoriteViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FindViewModel::class)
    fun bindFindViewModel(findViewModel: FindViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    fun bindDetailViewModel(detailViewModel: DetailViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PopularViewModel::class)
    fun bindPopularViewModel(popularViewModel: PopularViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopRatedViewModel::class)
    fun bindChildViewModel(topRatedViewModel: TopRatedViewModel) : ViewModel


    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}