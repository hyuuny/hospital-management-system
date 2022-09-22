package com.hyuuny.hospitalmanagementsystem.utils

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class CodeGenerator {

    fun generateCode(): String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyyMMdd${(1000..99999).random()}"))

}