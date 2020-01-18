package com.medialink.sub5close.data.tvShow

import android.util.Log
import com.google.gson.Gson
import com.medialink.sub4moviedb.model.tv_show.TvShowRespon
import com.medialink.sub5close.data.OperationCallback
import com.medialink.sub5close.model.error.ErrorRespon
import com.medialink.sub5close.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TvShowRepository : TvShowDataSource {

    private var call: Call<TvShowRespon>? = null

    override var page: Int = 1
    override var language: String = "en-US"
        get() {
            val iso3Country = Locale.getDefault().country
            val lang = Locale.getDefault().language

            if (lang.equals("in", ignoreCase = true)) {
                return "id-${iso3Country}"
            } else {
                return "${lang}-${iso3Country}"
            }
        }

    override fun getTvShows(callback: OperationCallback) {
        call = RetrofitClient.getApiTv().getTvPopular(page, language)
        call?.enqueue(object : Callback<TvShowRespon> {
            override fun onFailure(call: Call<TvShowRespon>, t: Throwable) {
                callback.onError(t)
                Log.d("debug", t.message.toString())
            }

            override fun onResponse(call: Call<TvShowRespon>, response: Response<TvShowRespon>) {
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