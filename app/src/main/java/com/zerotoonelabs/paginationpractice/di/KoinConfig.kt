package com.zerotoonelabs.paginationpractice.di

import com.zerotoonelabs.paginationpractice.BuildConfig
import com.zerotoonelabs.paginationpractice.R
import com.zerotoonelabs.paginationpractice.data.db.Db
import com.zerotoonelabs.paginationpractice.data.network.AppExecutors
import com.zerotoonelabs.paginationpractice.data.network.MovieApiService
import com.zerotoonelabs.paginationpractice.detail.DetailViewModel
import com.zerotoonelabs.paginationpractice.repository.DataRepository
import com.zerotoonelabs.paginationpractice.search.SearchViewModel
import com.zerotoonelabs.paginationpractice.util.UrlProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.HttpUrl
import org.koin.android.ext.koin.androidContext


val appModule = module {
    single {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", androidContext().getString(R.string.api_key))
                    .build()
                chain.proceed(
                    original.newBuilder()
                        .url(url)
                        .build()
                )
            }
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
        clientBuilder.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(UrlProvider.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }

    single { AppExecutors() }
    single { Db.getInstance(androidContext()).movieDao() }

    single<DataRepository>()
    viewModel<SearchViewModel>()
    viewModel<DetailViewModel>()
}
