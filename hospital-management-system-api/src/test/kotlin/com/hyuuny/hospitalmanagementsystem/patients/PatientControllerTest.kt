package com.hyuuny.hospitalmanagementsystem.patients

import com.hyuuny.hospitalmanagementsystem.FixturePatient.Companion.aPatientCreateRequest
import com.hyuuny.hospitalmanagementsystem.common.IntegrationTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

const val PATIENT_REQUEST_URL = "/api/v1/patients"

class PatientControllerTest : IntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @AfterEach
    fun afterEach() {
        patientRepository.deleteAll()
    }

    @Autowired
    lateinit var patientRepository: PatientRepository

    @Autowired
    lateinit var patientService: PatientService

    @Test
    fun `환자 등록`() {
        val request = aPatientCreateRequest()

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .log().all()
            .post(PATIENT_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

}