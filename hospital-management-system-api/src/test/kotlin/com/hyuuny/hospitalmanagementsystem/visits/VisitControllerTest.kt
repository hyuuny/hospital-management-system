package com.hyuuny.hospitalmanagementsystem.visits

import com.hyuuny.hospitalmanagementsystem.FixturePatient.Companion.aPatientCreateRequest
import com.hyuuny.hospitalmanagementsystem.FixtureVisit.Companion.aVisitCreateRequest
import com.hyuuny.hospitalmanagementsystem.common.IntegrationTest
import com.hyuuny.hospitalmanagementsystem.patients.PatientRepository
import com.hyuuny.hospitalmanagementsystem.patients.PatientService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

const val VISIT_REQUEST_URL = "/api/v1/{patientId}/visits"

class VisitControllerTest : IntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @AfterEach
    fun afterEach() {
        visitRepository.deleteAll()
        patientRepository.deleteAll()
    }

    @Autowired
    lateinit var visitRepository: VisitRepository

    @Autowired
    lateinit var visitService: VisitService

    @Autowired
    lateinit var patientRepository: PatientRepository

    @Autowired
    lateinit var patientService: PatientService

    @Test
    fun `환자 방문 기록 상세 조회`() {
        val savedPatient = patientService.createPatient(aPatientCreateRequest())
        val request = aVisitCreateRequest()
        val savedVisit = visitService.createVisit(savedPatient.id, request)

        Given {
            contentType(ContentType.JSON)
        } When {
            log()
            get("$VISIT_REQUEST_URL/{id}", savedPatient.id, savedVisit.id)
        } Then {
            log()
            statusCode(HttpStatus.OK.value())
            body(containsString("id"))
            body("hospitalId", equalTo(1))
            body("patientId", equalTo(savedPatient.id.toInt()))
            body("visitStatus", equalTo(request.visitStatus))
            body("diagnosisType", equalTo(request.diagnosisType))
            body(containsString("receptionDateTime"))
        }
    }

    @Test
    fun `환자 방문 기록 상세 조회 - 잘못된 아이디 예외`() {
        val savedPatient = patientService.createPatient(aPatientCreateRequest())

        Given {
            contentType(ContentType.JSON)
        } When {
            log()
            get("$VISIT_REQUEST_URL/{id}", savedPatient.id, 99999)
        } Then {
            body("code", equalTo(404))
            body("message", equalTo("환자 방문 이력을 찾을 수 없습니다."))
        }
    }

}