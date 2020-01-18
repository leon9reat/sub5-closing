package com.medialink.sub5close.data.movie

import com.medialink.sub5close.data.OperationCallback

interface MovieDataSource {
    var page: Int
    var language: String

    fun getMovies(callback: OperationCallback)
    fun cancel()
}