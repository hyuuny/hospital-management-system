package com.hyuuny.hospitalmanagementsystem.patients

import com.hyuuny.hospitalmanagementsystem.FixturePatient.Companion.aPatientCreateRequest
import com.hyuuny.hospitalmanagementsystem.common.IntegrationTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
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

    @Test
    fun `환자 수정`() {
        val request = aPatientCreateRequest()
        val savedPatient = patientService.createPatient(request)

        val updateRequest = PatientUpdateRequest(
            name = "성현치과",
            gender = "F",
            birthDay = "2000-12-31",
            mobilePhoneNumber = "010-7896-8012"
        )

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .`when`()
            .log().all()
            .put("$PATIENT_REQUEST_URL/{id}", savedPatient.id)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(containsString("id"))
            .assertThat().body("name", equalTo(updateRequest.name))
            .assertThat().body("gender", equalTo(updateRequest.gender))
            .assertThat().body("birthDay", equalTo(updateRequest.birthDay))
            .assertThat().body("mobilePhoneNumber", equalTo(updateRequest.mobilePhoneNumber))
    }

    @Test
    fun `환자 삭제`() {
        val request = aPatientCreateRequest()
        val savedPatient = patientService.createPatient(request)

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .delete("$PATIENT_REQUEST_URL/{id}", savedPatient.id)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `환자 상세 조회`() {
        val request = aPatientCreateRequest()
        val savedPatient = patientService.createPatient(request)

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$PATIENT_REQUEST_URL/{id}", savedPatient.id)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(containsString("id"))
            .assertThat().body("name", equalTo(request.name))
            .assertThat().body("gender", equalTo(request.gender))
            .assertThat().body("birthDay", equalTo(request.birthDay))
            .assertThat().body("mobilePhoneNumber", equalTo(request.mobilePhoneNumber))
    }

}