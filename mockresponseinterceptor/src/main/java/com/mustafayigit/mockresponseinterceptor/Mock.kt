package com.mustafayigit.mockresponseinterceptor

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Mock(
    val responseCode: Int = 200,
    val fileName: String = ""
)
