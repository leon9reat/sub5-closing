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
}