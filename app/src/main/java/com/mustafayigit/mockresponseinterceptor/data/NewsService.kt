package com.mustafayigit.mockresponseinterceptor.data

import com.mustafayigit.mockresponseinterceptor.BuildConfig
import com.mustafayigit.mockresponseinterceptor.data.model.NewsWrapperModel
import com.mustafayigit.mockresponseinterceptor.Mock
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Mustafa Yiğit on 12/06/2023
 * mustafa.yt65@gmail.com
 */
interface NewsService {
    @GET("top-headlines")
    @Mock
    suspend fun getNews(
        @Query("language") country: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): Response<NewsWrapperModel>

    @GET("top-headlines")
    @Mock(responseCode = 404)
    suspend fun getNewsError404(
        @Query("language") country: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): Response<NewsWrapperModel>

    @GET("top-headlines")
    @Mock(responseCode = 403)
    suspend fun getNewsError403(
        @Query("language") country: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): Response<NewsWrapperModel>

    @GET("top-headlines")
    @Mock(fileName = "test_file.json")
    suspend fun getNewsCustomFileName(
        @Query("language") country: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): Response<NewsWrapperModel>

    @GET("top-headlines")
    @Mock(fileName = "test_file.json", responseCode = 400)
    suspend fun getNewsCustomFileNameResponseCode(
        @Query("language") country: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): Response<NewsWrapperModel>
}