package com.hyuuny.hospitalmanagementsystem.exception

sealed class ServerException(
    val code: Int,
    override val message: String,
) : RuntimeException()

data class NotFountException(
    override val message: String,
) : ServerException(404, message)

data class BadRequestException(
    override val message: String,
) : ServerException(400, message)
