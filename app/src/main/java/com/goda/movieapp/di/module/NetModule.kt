package com.goda.movieapp.di.module

import com.goda.movieapp.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.goda.movieapp.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

@Module
class NetModule {

    private val connectionTimeout: Int = 10000

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(connectionTimeout.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                var originalHttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", Constants.API_KEY)
                    .addQueryParameter("language", Constants.LANGUAGE)
                    .build()
                val requestBuilder = original.newBuilder()
                    .url(url)
                chain.proceed(requestBuilder.build())
            }
            .build()
    }


    val httpLoggingInterceptor by lazy {
        return@lazy HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

}