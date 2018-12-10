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

package com.zerotoonelabs.paginationpractice.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zerotoonelabs.paginationpractice.GlideRequests
import com.zerotoonelabs.paginationpractice.R
import com.zerotoonelabs.paginationpractice.util.loadWithGlide
import com.zerotoonelabs.paginationpractice.vo.Movie

/**
 * A RecyclerView ViewHolder that displays a reddit movie.
 */
class MoviesViewHolder(
    view: View,
    private val glide: GlideRequests,
    private val clickListener: (movie: Movie) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.text_movie_title)
    private val overview: TextView = view.findViewById(R.id.text_overview)
    private val score: TextView = view.findViewById(R.id.text_rating)
    private val thumbnail: ImageView = view.findViewById(R.id.image_poster)
    private var movie: Movie? = null

    fun bind(movie: Movie?) {
        this.movie = movie
        title.text = movie?.title ?: "loading"
        overview.text = movie?.overview ?: "unknown"
        score.text = "${movie?.popularity ?: 0}"
        if (movie?.posterPath != null) {
            thumbnail.visibility = View.VISIBLE
            thumbnail.loadWithGlide(movie.posterPath, glide)
        } else {
            thumbnail.visibility = View.GONE
            glide.clear(thumbnail)
        }
        itemView.setOnClickListener {
            movie?.let {
                clickListener(it)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests, clickListener: (movie: Movie) -> Unit): MoviesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
            return MoviesViewHolder(view, glide, clickListener)
        }
    }
}