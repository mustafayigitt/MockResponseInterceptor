package com.mustafayigit.mockresponseinterceptor.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mustafa Yiğit on 12/06/2023
 * mustafa.yt65@gmail.com
 */
data class NewsModel(
    @SerializedName("title")
    val title: String,
    @SerializedName("urlToImage")
    val coverImage: String?
)