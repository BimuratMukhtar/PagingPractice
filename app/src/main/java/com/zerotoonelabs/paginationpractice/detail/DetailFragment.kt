package com.zerotoonelabs.paginationpractice.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.zerotoonelabs.paginationpractice.GlideApp
import com.zerotoonelabs.paginationpractice.R

import com.zerotoonelabs.paginationpractice.databinding.FragmentDetailBinding
import com.zerotoonelabs.paginationpractice.util.autoCleared
import com.zerotoonelabs.paginationpractice.util.loadWithGlide
import com.zerotoonelabs.paginationpractice.vo.Movie
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment() {

    private lateinit var selectedMovie: Movie
    private var binding: FragmentDetailBinding by autoCleared()
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedMovie = Gson().fromJson(it.getString(ARG_MOVIE), Movie::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.movie = selectedMovie
        val glide = GlideApp.with(this)
        binding.imagePoster.loadWithGlide(selectedMovie.posterPath, glide, true)

        viewModel.isFavorite.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.imageLike.setImageResource(R.drawable.ic_like_active)
            } else {
                binding.imageLike.setImageResource(R.drawable.ic_like_passive)
            }
        })

        binding.imageLike.setOnClickListener {
            if (viewModel.isFavorite.value == true) {
                viewModel.removeFromFavorite(selectedMovie.id)
            }else{
                viewModel.addToFavorite(selectedMovie.id)
            }
        }

        viewModel.getIsFavorite(selectedMovie.id)
    }

    companion object {
        @JvmStatic
        fun newInstance(movie: Movie) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MOVIE, Gson().toJson(movie, Movie::class.java))
                }
            }

        private const val ARG_MOVIE = "movieData"
        const val TAG = "detailFragment"
    }
}
