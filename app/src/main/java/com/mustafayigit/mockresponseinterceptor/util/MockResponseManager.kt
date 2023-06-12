package com.mustafayigit.mockresponseinterceptor.util

import android.content.Context
import android.util.Log
import okhttp3.Request
import retrofit2.Invocation
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

class MockResponseManager {

    fun getJsonByUrl(context: Context, request: Request): String {
        val file = (getFileNameFromRequest(request) + "_" + request.method.lowercase()).also {
            Log.d("MockResponseInterceptor", "Url: ${request.url} --> to $it.json")
        }
        return context.assets
            .open("$file.json")
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

        path = convertToJsonFileName(path)
        return path
    }

    private fun convertToJsonFileName(path: String): String {
        return path.replaceFirstChar {
            if (it == '/') "" else "$it"
        } // drop '/' character of before the endpoint
            .replace(Regex("[{}?/]"), "_") // Replace specific characters with '/'
            .replace(Regex("_{2}"), "_") // Replace specific characters with '/'
            .dropLastWhile { it == '_' }
            .lowercase()
    }
}