/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zerotoonelabs.paginationpractice.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.zerotoonelabs.paginationpractice.data.network.MovieApiService
import com.zerotoonelabs.paginationpractice.data.network.response.MoviesResponse
import com.zerotoonelabs.paginationpractice.vo.Movie
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

/**
 * A data source that uses the before/after keys returned in page requests.
 * <p>
 * See ItemKeyedSubredditDataSource
 */
class PageKeyedSubredditDataSource(
    private val redditApi: MovieApiService,
    private val query: String,
    private val retryExecutor: Executor) : PageKeyedDataSource<Int, Movie>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(
            params: LoadParams<Int>,
            callback: LoadCallback<Int, Movie>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>) {
        val request = redditApi.getMovies(
            query = query
        )
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync

        try {
            val response = request.execute()
            val data = response.body()
            if(response.isSuccessful){
                val items = data?.results ?: emptyList()
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                callback.onResult(items, null, getNextPageNumber(data))
            }else{
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(response.body()?.statusMessage ?: "unknown error")
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        redditApi.getMovies(query = query,
                page = params.key)
            .enqueue(
                object : retrofit2.Callback<MoviesResponse> {
                    override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                    }

                    override fun onResponse(
                            call: Call<MoviesResponse>,
                            response: Response<MoviesResponse>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            val items = data?.results ?: emptyList()
                            retry = null
                            callback.onResult(items, getNextPageNumber(data))
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            retry = {
                                loadAfter(params, callback)
                            }
                            networkState.postValue(
                                    NetworkState.error(response.body()?.statusMessage ?: "unknown error"))
                        }
                    }
                }
        )
    }

    private fun getNextPageNumber(data: MoviesResponse?): Int?{
        if(data == null)
            return null
        return if(data.page < data.totalPages){
            data.page + 1
        }else{
            null
        }
    }
}