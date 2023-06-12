package com.mustafayigit.mockresponseinterceptor.util


/**
 * Created by Mustafa Yiğit on 12/06/2023
 * mustafa.yt65@gmail.com
 */
sealed class State<out T> {
    data class Success<T>(val data: T) : State<T>()
    data class Error(val exception: Throwable) : State<Nothing>()
    object Loading : State<Nothing>()
}