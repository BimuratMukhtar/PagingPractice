package com.zerotoonelabs.paginationpractice.data.network

import com.zerotoonelabs.paginationpractice.data.network.response.MoviesResponse
import com.zerotoonelabs.paginationpractice.vo.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieApiService {
    @GET("search/movie")
    fun getMovies(@Query("query") query: String, @Query("page") page: Int? = null): Call<MoviesResponse>


}