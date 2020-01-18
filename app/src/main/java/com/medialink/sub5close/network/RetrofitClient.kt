package com.medialink.sub5close.network

import com.google.gson.Gson
import com.medialink.sub5close.BuildConfig
import com.medialink.sub5close.Consts
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client =
        if (BuildConfig.DEBUG) {
            OkHttpClient().newBuilder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logInterceptor)
                .build()
        } else {
            OkHttpClient().newBuilder()
                .addInterceptor(logInterceptor)
                .addInterceptor(authInterceptor)
                .build()

        }

    private val gson = Gson().newBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(Consts.TMDB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getApiMovie(): ApiMovie = retrofit.create(ApiMovie::class.java)

    fun getApiTv(): ApiTvShow = retrofit.create(ApiTvShow::class.java)
}