package com.ll.backend.global.globalExceptionHandler

import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.base.Empty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException::class)
    fun handleException(e: ServiceException): ResponseEntity<RsData<Empty>> {
        val rsData = e.getRsData()

        return ResponseEntity
            .status(rsData.statusCode)
            .body(rsData)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handle(ex: NoSuchElementException): ResponseEntity<RsData<Empty>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                RsData(
                    "404-1",
                    "해당 데이터가 존재하지 않습니다."
                )
            )
    }
}