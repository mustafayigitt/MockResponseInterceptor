package com.mustafayigit.mockresponseinterceptor.util

import retrofit2.Response

fun <T> Response<T>.asState(): State<T> {
    return if (isSuccessful) {
        body()?.let {
            State.Success(it)
        } ?: State.Error(Throwable("Body is null"))
    } else {
        State.Error(Throwable("Error code: ${code()}"))
    }
}