package com.mustafayigit.mockresponseinterceptor.util

import android.content.Context
import android.util.Log
import com.mustafayigit.mockresponseinterceptor.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Invocation
import java.io.FileNotFoundException
import javax.inject.Inject

class MockResponseInterceptor @Inject constructor(
    @ApplicationContext
    private val appContext: Context,
) : Interceptor {
    private val mockResponseManager = MockResponseManager()

    override fun intercept(chain: Interceptor.Chain): Response {
        val initialRequest = chain.request()

        val mockAnnotation = initialRequest
            .tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(Mock::class.java)

        val initialResponse = if (
            EnvironmentManager.isMockingEnabled() && mockAnnotation != null
        ) {
            getMockResponse(appContext, initialRequest)
        } else {
            chain.proceed(initialRequest)
        }
        return initialResponse
    }

    private fun getMockResponse(context: Context, request: Request): Response {
        Log.d("MockResponseInterceptor", "Ta daa! MockResponseInterceptor running...")
        val jsonString = kotlin.runCatching {
            mockResponseManager.getJsonByUrl(context, request)
        }.onFailure {
            if (it is FileNotFoundException && BuildConfig.DEBUG) {
                error("MockResponseInterceptor: File not found for url: ${request.url}")
            }
        }.getOrThrow()
        val mockBody = jsonString.toResponseBody("application/json".toMediaType())

        return Response.Builder()
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .code(200)
            .message(mockBody.toString())
            .body(mockBody)
            .build()
    }
}