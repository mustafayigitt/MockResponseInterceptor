package com.mustafayigit.mockresponseinterceptor

import android.content.res.AssetManager
import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Invocation
import java.io.FileNotFoundException

class MockResponseInterceptor private constructor(
    assetManager: AssetManager,
    private val isGlobalMockingEnabled: () -> Boolean = { true },
    fileNameExtractor: ((String) -> String)? = null
) : Interceptor {
    private val mockResponseManager = MockResponseManager(
        assetManager,
        fileNameExtractor
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val initialRequest = chain.request()
        val isMockingEnabled = isGlobalMockingEnabled()
        if (isMockingEnabled.not()) {
            return chain.proceed(initialRequest)
        }

        val method = initialRequest.tag(Invocation::class.java)?.method()
        val annotation = method?.getAnnotation(Mock::class.java)
        if (annotation != null) {
            return getMockResponse(initialRequest, annotation.fileName, annotation.responseCode)
        }
        return chain.proceed(initialRequest)
    }

    private fun getMockResponse(request: Request, fileName: String, responseCode: Int): Response {
        Log.d("MockResponseInterceptor", "Ta daa! MockResponseInterceptor running...")
        val jsonString = kotlin.runCatching {
            mockResponseManager.getJsonByUrl(request, fileName, responseCode)
        }.onFailure {
            if (it is FileNotFoundException && BuildConfig.DEBUG) {
                error("MockResponseInterceptor: File not found for url: ${request.url}")
            }
        }.getOrThrow()
        val mockBody = ResponseBody.create("application/json".toMediaTypeOrNull(), jsonString)

        return Response.Builder()
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .code(responseCode)
            .message(mockBody.toString())
            .body(mockBody)
            .build()
    }

    class Builder(private val assetManager: AssetManager) {
        private var isMockingEnabled: () -> Boolean = { BuildConfig.DEBUG }
        private var fileNameExtractor: ((String) -> String)? = null

        fun isGlobalMockingEnabled(isMockingEnabled: () -> Boolean) = apply {
            this.isMockingEnabled = isMockingEnabled
        }

        fun fileNameExtractor(fileNameExtractor: ((String) -> String)?) = apply {
            this.fileNameExtractor = fileNameExtractor
        }

        fun build() = MockResponseInterceptor(assetManager, isMockingEnabled, fileNameExtractor)
    }
}