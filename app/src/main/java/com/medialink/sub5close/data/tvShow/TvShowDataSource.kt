package com.medialink.sub5close.data.tvShow

import com.medialink.sub5close.data.OperationCallback

interface TvShowDataSource {

    var page: Int
    var language: String

    fun getTvShows(callback: OperationCallback)
    fun findTvShow(callback: OperationCallback, query: String)
    fun cancel()
}