package com.ll.backend.global.rsData

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RsData<T>(
    val resultCode: String,
    val msg: String,
    val data: T? = null
) {
    constructor(resultCode: String, msg: String) : this(resultCode, msg, null)

    val statusCode: Int
        get() = resultCode.split("-", limit = 2)[0].toInt()
}