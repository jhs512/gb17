package com.ll.backend.global.rsData

data class RsData<T>(
    val resultCode: String,
    val msg: String,
    val data: T? = null
) {
    constructor(resultCode: String, msg: String) : this(resultCode, msg, null)
}