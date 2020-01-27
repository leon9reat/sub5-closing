package com.medialink.sub5close.data.movie

import com.medialink.sub4moviedb.model.movie.Movie
import com.medialink.sub4moviedb.model.movie.MovieRespon
import com.medialink.sub5close.data.OperationCallback

interface MovieDataSource {
    var page: Int
    var language: String
    var searchText: String

    fun getMovies(callback: OperationCallback)
    fun findMovies(callback: OperationCallback, query: String)
    suspend fun findMovieToday(page: Int, language: String, drTgl: String, spTgl: String) : MovieRespon
    fun cancel()
}