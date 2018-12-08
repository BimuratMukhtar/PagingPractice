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
        dataRepo.postsOfSubreddit(it, 30)
    }
    val posts = Transformations.switchMap(repoResult, { it.pagedList })!!
    val networkState = Transformations.switchMap(repoResult, { it.networkState })!!
    val refreshState = Transformations.switchMap(repoResult, { it.refreshState })!!


    fun getMovies(query: String): Boolean {
        if (_query.value == query) {
            return false
        }
        _query.value = query
        return true
    }
}
