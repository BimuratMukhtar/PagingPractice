package com.zerotoonelabs.paginationpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.zerotoonelabs.paginationpractice.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navigateSearchFragment()
    }

    private fun navigateSearchFragment(){
        val fragment = SearchFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(SearchFragment.TAG)
            .replace(R.id.fl_container, fragment, SearchFragment.TAG)
            .commitAllowingStateLoss()
    }
}
