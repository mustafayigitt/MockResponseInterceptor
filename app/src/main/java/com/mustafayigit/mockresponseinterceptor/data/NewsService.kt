package com.mustafayigit.mockresponseinterceptor.data

import com.google.gson.JsonObject
import com.mustafayigit.mockresponseinterceptor.BuildConfig
import com.mustafayigit.mockresponseinterceptor.data.model.NewsModel
import com.mustafayigit.mockresponseinterceptor.data.model.NewsWrapperModel
import retrofit2.Response
import retrofit2.http.GET


/**
 * Created by Mustafa Yiğit on 12/06/2023
 * mustafa.yt65@gmail.com
 */
interface NewsService {

    @GET("top-headlines?language=en&apiKey=${BuildConfig.NEWS_API_KEY}")
    suspend fun getNews(): Response<NewsWrapperModel>
}