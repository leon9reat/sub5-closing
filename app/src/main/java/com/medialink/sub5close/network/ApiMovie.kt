package com.medialink.sub5close.network

import com.medialink.sub4moviedb.model.movie.MovieRespon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMovie {
    @GET("movie/popular")
    fun getMoviePopular(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Call<MovieRespon>
}