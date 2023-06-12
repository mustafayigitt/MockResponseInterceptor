package com.mustafayigit.mockresponseinterceptor.util

import android.content.res.AssetManager
import android.util.Log
import okhttp3.Request
import retrofit2.Invocation
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

class MockResponseManager(
    private val assetManager: AssetManager,
    private val fileNameExtractor: ((String) -> String)? = null,
) {
    fun getJsonByUrl(request: Request): String {
        val fileName = getFileNameFromRequest(request) + "_" + request.method.lowercase()
        Log.d("MockResponseInterceptor", "Url: ${request.url} --> to $fileName.json")

        return assetManager
            .open("$fileName.json")
            .bufferedReader()
            .use { it.readText() }
    }

    private fun getFileNameFromRequest(request: Request): String {
        val ann =
            request.tag(Invocation::class.java)?.method()?.annotations.orEmpty().firstOrNull {
                it is GET || it is POST || it is PUT || it is DELETE || it is PATCH
            }

        var path = (ann as? GET)?.value
            ?: (ann as? POST)?.value
            ?: (ann as? PUT)?.value
            ?: (ann as? DELETE)?.value
            ?: (ann as? PATCH)?.value
            ?: ""

        Log.d("MockResponseInterceptor", "Full Path Found: $path")

        path = fileNameExtractor?.invoke(path) ?: convertToJsonFileName(path)
        return path
    }

    private fun convertToJsonFileName(path: String): String {
        return path
            .replaceFirstChar { if (it == '/') "" else "$it" }
            .replace(Regex("[{}?/]"), "_")
            .replace(Regex("_{2}"), "_")
            .dropLastWhile { it == '_' }
            .lowercase()
    }
}