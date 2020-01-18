package com.medialink.sub5close.model.error

import com.google.gson.annotations.SerializedName

data class ErrorRespon(
    @field:SerializedName("status_message")
    val statusMessage: String? = null,
    @field:SerializedName("status_code")
    val statusCode: Int? = null,
    @field:SerializedName("success")
    val success: Boolean? = null
)