package com.zerotoonelabs.paginationpractice.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.zerotoonelabs.paginationpractice.data.db.MovieDao
import com.zerotoonelabs.paginationpractice.data.db.MovieFavorite
import com.zerotoonelabs.paginationpractice.data.network.AppExecutors
import com.zerotoonelabs.paginationpractice.data.network.MovieApiService
import com.zerotoonelabs.paginationpractice.vo.Movie

class DataRepository(
    private val apiService: MovieApiService,
    private val executors: AppExecutors,
    private val movieDao: MovieDao
) {

    @MainThread
    fun moviesByQuery(subReddit: String, pageSize: Int): Listing<Movie> {
        val sourceFactory = SubRedditDataSourceFactory(apiService, subReddit, executors.networkIO())

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = sourceFactory.toLiveData(
            pageSize = pageSize,
            // provide custom executor for network requests, otherwise it will default to
            // Arch Components' IO pool which is also used for disk access
            fetchExecutor = executors.networkIO()
        )

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }

    fun getFavoriteByMovieId(movieId: Int, callback : (contains: Boolean) -> Unit){
        executors.diskIO().execute {
            val res = movieDao.isFavorite(movieId)
            callback(res != null)
        }
    }

    fun addToFavorite(movieId: Int, callback : (success: Boolean) -> Unit){
        executors.diskIO().execute {
            val res = movieDao.addToFavorite(MovieFavorite(movieId))
            callback(res != null)
        }
    }

    fun deleteFromFavorite(movieId: Int, callback : (success: Boolean) -> Unit){
        executors.diskIO().execute {
            val res = movieDao.removeFromFavorite(movieId)
            callback(res != 0)
        }
    }
}