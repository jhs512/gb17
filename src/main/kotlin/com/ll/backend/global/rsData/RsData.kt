package com.ll.backend.global.rsData

import com.fasterxml.jackson.annotation.JsonInclude
import com.ll.backend.standard.base.Empty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RsData<T>(
    val resultCode: String,
    val msg: String,
    val data: T
) {
    constructor(resultCode: String, msg: String) : this(resultCode, msg, Empty() as T)

    val statusCode: Int
        get() = resultCode.split("-", limit = 2)[0].toInt()
}