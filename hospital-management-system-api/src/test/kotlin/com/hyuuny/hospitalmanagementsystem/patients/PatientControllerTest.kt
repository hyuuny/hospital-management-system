package com.hyuuny.hospitalmanagementsystem.patients

import com.hyuuny.hospitalmanagementsystem.FixturePatient.Companion.aPatientCreateRequest
import com.hyuuny.hospitalmanagementsystem.common.IntegrationTest
import com.hyuuny.hospitalmanagementsystem.visits.VisitCreateRequest
import com.hyuuny.hospitalmanagementsystem.visits.VisitRepository
import com.hyuuny.hospitalmanagementsystem.visits.VisitService
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import java.util.stream.IntStream

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
        visitRepository.deleteAll()
        patientRepository.deleteAll()
    }

    @Autowired
    lateinit var patientRepository: PatientRepository

    @Autowired
    lateinit var patientService: PatientService

    @Autowired
    lateinit var visitRepository: VisitRepository

    @Autowired
    lateinit var visitService: VisitService

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

    @Test
    fun `환자 상세 조회 - 잘못된 아이디 예외`() {
        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$PATIENT_REQUEST_URL/{id}", 999999)
            .then()
            .log().all()
            .assertThat().body("code", equalTo(404))
            .assertThat().body("message", equalTo("환자를 찾을 수 없습니다."))
    }

    @Test
    fun `환자 상세 조회 - 방문 기록도 같이 조회`() {
        val request = aPatientCreateRequest()
        val savedPatient = patientService.createPatient(request)

        IntStream.range(0, 5).forEach {
            run {
                val requestVisit = VisitCreateRequest(
                    hospitalId = 1,
                    visitStatus = "VISITING",
                    diagnosisType = "D"
                )
                visitService.createVisit(savedPatient.id, requestVisit)
            }
        }

        Given {
            contentType(ContentType.JSON)
        } When {
            log().all()
            get("$PATIENT_REQUEST_URL/{ia}", savedPatient.id)
        }Then {
            log().all()
            assertThat().body(containsString("id"))
            assertThat().body("name", equalTo(request.name))
            assertThat().body("gender", equalTo(request.gender))
            assertThat().body("birthDay", equalTo(request.birthDay))
            assertThat().body("mobilePhoneNumber", equalTo(request.mobilePhoneNumber))
            assertThat().body("visits[0].patientId", equalTo(savedPatient.id.toInt()))
            assertThat().body("visits[0].visitStatus", equalTo("VISITING"))
            assertThat().body("visits[0].diagnosisType", equalTo("D"))
            assertThat().body("visits.size()", equalTo(5))
        }

    }

    @Test
    fun `환자 목록 조회`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                val request = PatientCreateRequest(
                    hospitalId = 1L,
                    name = "${value}위 성현내과",
                    gender = "M",
                    birthDay = "1993-01-01",
                    mobilePhoneNumber = "010-1234-1234",
                )
                patientService.createPatient(request)
            }
        }

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get(PATIENT_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", IsEqual.equalTo(10))
            .assertThat().body("page.totalElements", IsEqual.equalTo(11))
            .assertThat().body("page.totalPages", IsEqual.equalTo(2))
            .assertThat().body("page.number", IsEqual.equalTo(0))
    }

}