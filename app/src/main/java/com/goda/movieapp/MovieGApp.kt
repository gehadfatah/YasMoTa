package com.goda.movieapp

import androidx.appcompat.app.AppCompatDelegate
import com.goda.movieapp.common.ApplicationIntegration
import com.goda.movieapp.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MovieGApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this)!!.build()


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        ApplicationIntegration.with(this)

    }


}
