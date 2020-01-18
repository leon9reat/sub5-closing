package com.medialink.sub5close.data.movie

import android.util.Log
import com.google.gson.Gson
import com.medialink.sub4moviedb.model.movie.MovieRespon
import com.medialink.sub5close.data.OperationCallback
import com.medialink.sub5close.model.error.ErrorRespon
import com.medialink.sub5close.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MovieRepository : MovieDataSource {

    private var call: Call<MovieRespon>? = null

    override var page: Int = 1
    override var language: String = "en-US"
        get() {
            val iso3Country = Locale.getDefault().country
            val lokal = Locale.getDefault().language

            if (lokal.equals("in", ignoreCase = true)) {
                return "id-${iso3Country}"
            } else {
                return "${lokal}-${iso3Country}"
            }
        }

    override fun getMovies(callback: OperationCallback) {
        call = RetrofitClient.getApiMovie().getMoviePopular(page, language)
        call?.enqueue(object : Callback<MovieRespon> {
            override fun onFailure(call: Call<MovieRespon>, t: Throwable) {
                callback.onError(t)
                Log.d("debug", t.message.toString())
            }

            override fun onResponse(call: Call<MovieRespon>, response: Response<MovieRespon>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()?.results)
                    Log.d("debug", "success: ${response.body()?.totalResults}")
                } else {
                    val jsonString = response.errorBody()?.charStream()?.readText() ?: "{}"
                    val error404 = Gson().fromJson(jsonString, ErrorRespon::class.java)
                    when (response.code()) {
                        401 -> callback.onError(error404.statusMessage)
                        404 -> callback.onError(error404.statusMessage)
                        else -> callback.onError(jsonString)
                    }
                }
            }

        })
    }

    override fun cancel() {
        call?.cancel()
    }
}