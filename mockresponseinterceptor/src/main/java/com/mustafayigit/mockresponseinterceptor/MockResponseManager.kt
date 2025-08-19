package com.mustafayigit.mockresponseinterceptor

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
    fun getJsonByUrl(request: Request, fileName: String, responseCode: Int): String {
        var mFilename = fileName
        if (mFilename.isNotEmpty()) {
            mFilename.replace(".json", "")
            Log.d("MockResponseInterceptor", "Url: ${request.url} --> to $mFilename")
            return assetManager
                .open(mFilename)
                .bufferedReader()
                .use { it.readText() }
        } else {
            mFilename = getFileNameFromRequest(request) + "_" + request.method
                .lowercase() + "_" + responseCode
            Log.d("MockResponseInterceptor", "Url: ${request.url} --> to $mFilename")
        }

        return assetManager
            .open("$mFilename.json")
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
        Log.d("MockResponseInterceptor", "File Path: $path")

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