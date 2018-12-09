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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.zerotoonelabs.paginationpractice.GlideRequests
import com.zerotoonelabs.paginationpractice.R
import com.zerotoonelabs.paginationpractice.util.UrlProvider
import com.zerotoonelabs.paginationpractice.vo.Movie

/**
 * A RecyclerView ViewHolder that displays a reddit post.
 */
class RedditPostViewHolder(view: View, private val glide: GlideRequests) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.text_movie_title)
    private val overview: TextView = view.findViewById(R.id.text_overview)
    private val score: TextView = view.findViewById(R.id.text_rating)
    private val thumbnail: ImageView = view.findViewById(R.id.image_poster)
    private var post: Movie? = null

    init {
        view.setOnClickListener {
            post?.let { url ->

            }
        }
    }

    fun bind(post: Movie?) {
        this.post = post
        title.text = post?.title ?: "loading"
        overview.text = post?.overview ?: "unknown"
        score.text = "${post?.popularity ?: 0}"
        if (post?.posterPath != null) {
            thumbnail.visibility = View.VISIBLE
            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 10f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            glide.load(UrlProvider.IMAGE_BASE_URL_LOW_RESOLUTION + post.posterPath)
                .centerInside()
                .placeholder(circularProgressDrawable)
                .into(thumbnail)
        } else {
            thumbnail.visibility = View.GONE
            glide.clear(thumbnail)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): RedditPostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
            return RedditPostViewHolder(view, glide)
        }
    }
}