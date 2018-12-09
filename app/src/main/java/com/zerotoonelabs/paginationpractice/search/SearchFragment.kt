package com.zerotoonelabs.paginationpractice.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.zerotoonelabs.paginationpractice.GlideApp

import com.zerotoonelabs.paginationpractice.R
import com.zerotoonelabs.paginationpractice.repository.NetworkState
import com.zerotoonelabs.paginationpractice.vo.Movie
import kotlinx.android.synthetic.main.search_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        const val TAG = "search"
        fun newInstance() = SearchFragment()
        const val KEY_QUERY = "movie"
    }

    private val model: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.search_fragment, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        initSwipeToRefresh()
        initSearch()
        val previosQuery = savedInstanceState?.getString(KEY_QUERY)
        if(!previosQuery.isNullOrEmpty()){
            model.showMovies(previosQuery)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_QUERY, model.currentQuery())
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = PostsAdapter(glide) {
            model.retry()
        }

        val columnCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            1
        } else {
            2
        }
        recycler_search.layoutManager = GridLayoutManager(requireContext(), columnCount)
        recycler_search.adapter = adapter
        model.movies.observe(this, Observer<PagedList<Movie>> {
            adapter.submitList(it)
        })
        model.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun initSwipeToRefresh() {
        model.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        swipe_refresh.setOnRefreshListener {
            model.refresh()
        }
    }

    private fun initSearch() {
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                updateMoviesFromInput()
            }
            true
        }
        input.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateMoviesFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateMoviesFromInput() {
        input.text.trim().toString().let {
            if (it.isNotEmpty()) {
                if (model.showMovies(it)) {
                    recycler_search.scrollToPosition(0)
                    (recycler_search.adapter as? PostsAdapter)?.submitList(null)
                }
            }
        }
    }
}
