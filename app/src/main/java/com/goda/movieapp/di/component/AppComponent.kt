package com.goda.movieapp.di.component

import com.goda.movieapp.MovieGApp
import com.goda.movieapp.di.module.ActivityModule
import com.goda.movieapp.di.module.AppModule
import com.goda.movieapp.di.module.NetModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityModule::class,
    AppModule::class,
    NetModule::class
])
interface AppComponent : AndroidInjector<MovieGApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MovieGApp): Builder?

        fun build(): AppComponent
    }

    override fun inject(application: MovieGApp)

}


