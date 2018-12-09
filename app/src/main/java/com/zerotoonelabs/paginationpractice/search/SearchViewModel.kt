package com.zerotoonelabs.paginationpractice.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.zerotoonelabs.paginationpractice.repository.DataRepository

class SearchViewModel(
    private val dataRepo: DataRepository
) : ViewModel() {

    private val _query = MutableLiveData<String>()
    private val repoResult = Transformations.map(_query) {
        dataRepo.moviesByQuery(it, 30)
    }
    val movies = Transformations.switchMap(repoResult) { it.pagedList }!!
    val networkState = Transformations.switchMap(repoResult) { it.networkState }!!
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }!!

    fun showMovies(query: String): Boolean {
        if (_query.value == query) {
            return false
        }
        _query.value = query
        return true
    }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }

    fun currentQuery(): String?{
        return _query.value
    }
}
