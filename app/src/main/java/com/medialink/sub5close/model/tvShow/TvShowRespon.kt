package com.medialink.sub4moviedb.model.tv_show

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShowRespon(
    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("total_pages")
    val totalPages: Int? = null,

    @field:SerializedName("results")
    val results: List<TvShow?>? = null,

    @field:SerializedName("total_results")
    val totalResults: Int? = null
) : Parcelable 