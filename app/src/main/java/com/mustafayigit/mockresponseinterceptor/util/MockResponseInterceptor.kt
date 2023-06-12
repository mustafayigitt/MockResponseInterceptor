package com.mustafayigit.mockresponseinterceptor.util

import android.content.Context
import android.util.Log
import com.mustafayigit.mockresponseinterceptor.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Invocation
import java.io.FileNotFoundException

class MockResponseInterceptor private constructor(
    private val context: Context,
    private val isMockingEnabled: () -> Boolean = { BuildConfig.DEBUG },
    fileNameExtractor: ((String) -> String)? = null
) : Interceptor {
    private val mockResponseManager = MockResponseManager(fileNameExtractor)

    override fun intercept(chain: Interceptor.Chain): Response {
        val initialRequest = chain.request()
        val isMockingEnabled = isMockingEnabled()
        if (isMockingEnabled.not()) {
            return chain.proceed(initialRequest)
        }

        initialRequest
            .tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(Mock::class.java) ?: return chain.proceed(initialRequest)

        return getMockResponse(context, initialRequest)
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

    class Builder(private val context: Context) {
        private var isMockingEnabled: () -> Boolean = { BuildConfig.DEBUG }
        private var fileNameExtractor: ((String) -> String)? = null

        fun isGlobalMockingEnabled(isMockingEnabled: () -> Boolean) = apply {
            this.isMockingEnabled = isMockingEnabled
        }

        fun fileNameExtractor(fileNameExtractor: ((String) -> String)?) = apply {
            this.fileNameExtractor = fileNameExtractor
        }

        fun build() = MockResponseInterceptor(context, isMockingEnabled, fileNameExtractor)
    }
}