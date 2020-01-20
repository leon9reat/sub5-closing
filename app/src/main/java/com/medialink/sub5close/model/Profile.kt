package com.medialink.sub5close.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    val id: Int,
    val userName: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val dob: String
) : Parcelable 