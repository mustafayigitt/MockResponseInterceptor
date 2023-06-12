package com.mustafayigit.mockresponseinterceptor.data

import com.mustafayigit.mockresponseinterceptor.data.model.NewsModel
import com.mustafayigit.mockresponseinterceptor.util.State
import com.mustafayigit.mockresponseinterceptor.util.asState
import javax.inject.Inject


/**
 * Created by Mustafa Yiğit on 12/06/2023
 * mustafa.yt65@gmail.com
 */
class NewsRepository @Inject constructor(
    private val newsService: NewsService
) : INewsRepository {

    override suspend fun getNews(): State<List<NewsModel>> {
        val result = newsService.getNews().asState()
        val data = (result as? State.Success)?.data?.articles
            ?: return State.Error(Exception("Error"))
        return State.Success(data)
    }
}