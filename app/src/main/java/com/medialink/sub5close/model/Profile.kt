package com.medialink.sub5close.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    val id: Int,
    val userName: String,
    val countPhotos: Int,
    val countFollower: Int,
    val countFollowing: Int,
    val email: String,
    val phone: String
) : Parcelable 