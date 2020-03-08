package com.tb.myskilllane.model

import com.google.gson.annotations.SerializedName

data class DataResponse(
    @SerializedName("author")
    var author: String,
    @SerializedName("download_url")
    var downloadUrl: String,
    @SerializedName("height")
    var height: Int,
    @SerializedName("id")
    var id: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("width")
    var width: Int
)