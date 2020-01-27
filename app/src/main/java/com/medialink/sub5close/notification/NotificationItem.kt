package com.medialink.sub5close.notification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class NotificationItem(
    var id: Int,
    var sender: String,
    var message: String
) : Parcelable {
}