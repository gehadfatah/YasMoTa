package com.goda.movieapp.di.module

import com.goda.movieapp.view.ui.child.TopRatedFragment
import com.goda.movieapp.view.ui.detail.DetailFragment
import com.goda.movieapp.view.ui.favorite.FavoriteFragment
import com.goda.movieapp.view.ui.find.FindFragment
import com.goda.movieapp.view.ui.home.HomeFragment
import com.goda.movieapp.view.ui.popular.PopularFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {

    @ContributesAndroidInjector
    fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    fun contributeFindFragment(): FindFragment

    @ContributesAndroidInjector
    fun contributePopularFragment(): PopularFragment

    @ContributesAndroidInjector
    fun contributeTopRatedFragment(): TopRatedFragment

    @ContributesAndroidInjector
    fun contributeDetailFragment(): DetailFragment

}