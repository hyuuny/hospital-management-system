package com.hyuuny.hospitalmanagementsystem.common

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode
import org.springframework.test.context.junit.jupiter.SpringExtension

@TestConstructor(autowireMode = AutowireMode.ALL)
@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = ["spring.config.location="
            + "classpath:application.yml,"
            + "classpath:application-test.yml"]
)
class IntegrationTest {
}