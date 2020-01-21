package com.medialink.sub5close.data.movie

import com.medialink.sub5close.data.OperationCallback

interface MovieDataSource {
    var page: Int
    var language: String
    var searchText: String

    fun getMovies(callback: OperationCallback)
    fun findMovies(callback: OperationCallback, query: String)
    fun cancel()
}