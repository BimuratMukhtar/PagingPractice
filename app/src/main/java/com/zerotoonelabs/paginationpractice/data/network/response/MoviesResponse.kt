package com.zerotoonelabs.paginationpractice.data.network.response

import com.google.gson.annotations.SerializedName
import com.zerotoonelabs.paginationpractice.vo.Movie

class MoviesResponse(
    val page: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    val results: List<Movie> = emptyList(),
    @SerializedName("status_code")
    val statusCode: Int = 1,
    @SerializedName("status_message")
    val statusMessage: String = "",
    val success: Boolean = true
)