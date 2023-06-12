package com.mustafayigit.mockresponseinterceptor.data

import com.mustafayigit.mockresponseinterceptor.data.model.NewsModel
import com.mustafayigit.mockresponseinterceptor.util.State


/**
 * Created by Mustafa Yiğit on 12/06/2023
 * mustafa.yt65@gmail.com
 */
interface INewsRepository {

    suspend fun getNews(): State<List<NewsModel>>
}