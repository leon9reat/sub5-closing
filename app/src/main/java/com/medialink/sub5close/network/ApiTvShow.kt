package com.medialink.sub5close.network

import com.medialink.sub4moviedb.model.tv_show.TvShowRespon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiTvShow {
    @GET("tv/popular")
    fun getTvPopular(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Call<TvShowRespon>

    // https://api.themoviedb.org/3/search/tv?api_key={api_key}&language=en-US&query=throne&page=1
    @GET("search/tv")
    fun findTvPopular(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("query") query: String = ""
    ): Call<TvShowRespon>
}