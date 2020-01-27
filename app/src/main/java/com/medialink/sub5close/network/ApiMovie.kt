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

    // https://api.themoviedb.org/3/search/movie?api_key={api}&language=en-US&query={query}&page=3
    @GET("search/movie")
    fun findMoviePopular(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("query") query: String = ""
    ): Call<MovieRespon>

    @GET("discover/movie")
    suspend fun getMovieRelease(
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("primary_release_date.gte") dr_tgl: String,
        @Query("primary_release_date.lte") sp_tgl: String
    ): MovieRespon
}