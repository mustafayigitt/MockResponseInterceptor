package com.mustafayigit.mockresponseinterceptor.di

import android.content.Context
import com.mustafayigit.mockresponseinterceptor.data.INewsRepository
import com.mustafayigit.mockresponseinterceptor.data.NewsRepository
import com.mustafayigit.mockresponseinterceptor.data.NewsService
import com.mustafayigit.mockresponseinterceptor.ui.MainActivity
import com.mustafayigit.mockresponseinterceptor.MockResponseInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Created by Mustafa Yiğit on 12/06/2023
 * mustafa.yt65@gmail.com
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideMockResponseInterceptor(
        @ApplicationContext context: Context
    ): MockResponseInterceptor {
        return MockResponseInterceptor.Builder(context.assets)
            .isGlobalMockingEnabled { MainActivity.isGlobalMockingEnabled }
            .build()
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        mockResponseInterceptor: MockResponseInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(mockResponseInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsService(
        retrofit: Retrofit
    ): NewsService {
        return retrofit.create(NewsService::class.java)
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModuleBinds {

    @Binds
    abstract fun bindNewsRepository(
        newsRepository: NewsRepository
    ): INewsRepository
}