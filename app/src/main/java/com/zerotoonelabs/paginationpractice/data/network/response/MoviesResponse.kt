package com.zerotoonelabs.paginationpractice.data.network.response

import com.google.gson.annotations.SerializedName
import com.zerotoonelabs.paginationpractice.vo.Movie

class MoviesResponse(
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPager: Int,
    val results: List<Movie>
)