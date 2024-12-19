package com.ll.backend.global.exceptions

import com.ll.backend.global.rsData.RsData

class ServiceException(resultCode: String, msg: String) : RuntimeException("$resultCode : $msg") {
    private val resultCode: String
        get() {
            message?.split(":", limit = 2).let {
                return it?.get(0)?.trim() ?: ""
            }
        }

    private val msg: String
        get() {
            message?.split(":", limit = 2).let {
                return it?.get(1)?.trim() ?: ""
            }
        }

    fun getRsData(): RsData<Void> {
        return RsData(resultCode, msg)
    }
}