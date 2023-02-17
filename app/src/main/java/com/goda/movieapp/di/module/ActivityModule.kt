package com.goda.movieapp.di.module


import com.goda.movieapp.view.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun contributeHomeActivity(): HomeActivity
}