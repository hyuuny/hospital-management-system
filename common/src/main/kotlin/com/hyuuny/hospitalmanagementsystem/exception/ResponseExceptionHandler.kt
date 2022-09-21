package com.hyuuny.hospitalmanagementsystem.exception

import mu.KotlinLogging
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ResponseExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ServerException::class)
    fun handleServerException(e: ServerException): ErrorResponseDto {
        logger.error { e.message }
        return ErrorResponseDto(code = e.code, message = e.message)
    }

}