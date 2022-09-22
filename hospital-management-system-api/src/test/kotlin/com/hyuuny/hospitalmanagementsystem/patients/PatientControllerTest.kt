package com.hyuuny.hospitalmanagementsystem.patients

import com.hyuuny.hospitalmanagementsystem.FixturePatient.Companion.aPatientCreateRequest
import com.hyuuny.hospitalmanagementsystem.common.IntegrationTest
import com.hyuuny.hospitalmanagementsystem.visits.VisitCreateRequest
import com.hyuuny.hospitalmanagementsystem.visits.VisitRepository
import com.hyuuny.hospitalmanagementsystem.visits.VisitService
import io.restassured.RestAssured
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
import java.time.LocalDate
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

        Given {
            contentType(ContentType.JSON)
            body(request)
        } When {
            log().all()
            post(PATIENT_REQUEST_URL)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
        }
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

        Given {
            contentType(ContentType.JSON)
            body(updateRequest)
        } When {
            log().all()
            put("$PATIENT_REQUEST_URL/{id}", savedPatient.id)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
            assertThat().body(containsString("id"))
            assertThat().body("name", equalTo(updateRequest.name))
            assertThat().body("gender", equalTo(updateRequest.gender))
            assertThat().body("birthDay", equalTo(updateRequest.birthDay))
            assertThat().body("mobilePhoneNumber", equalTo(updateRequest.mobilePhoneNumber))
        }
    }

    @Test
    fun `환자 삭제`() {
        val request = aPatientCreateRequest()
        val savedPatient = patientService.createPatient(request)

        Given {
            contentType(ContentType.JSON)
        } When {
            log().all()
            delete("$PATIENT_REQUEST_URL/{id}", savedPatient.id)
        } Then {
            log().all()
            statusCode(HttpStatus.NO_CONTENT.value())
        }
    }

    @Test
    fun `환자 상세 조회`() {
        val request = aPatientCreateRequest()
        val savedPatient = patientService.createPatient(request)

        Given {
            contentType(ContentType.JSON)
        } When {
            log().all()
            get("$PATIENT_REQUEST_URL/{id}", savedPatient.id)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
            assertThat().body(containsString("id"))
            assertThat().body("name", equalTo(request.name))
            assertThat().body("gender", equalTo(request.gender))
            assertThat().body("birthDay", equalTo(request.birthDay))
            assertThat().body("mobilePhoneNumber", equalTo(request.mobilePhoneNumber))
        }
    }

    @Test
    fun `환자 상세 조회 - 잘못된 아이디 예외`() {
        Given {
            contentType(ContentType.JSON)
        } When {
            log().all()
            get("$PATIENT_REQUEST_URL/{id}", 999999)
        } Then {
            log().all()
            assertThat().body("code", equalTo(404))
            assertThat().body("message", equalTo("환자를 찾을 수 없습니다."))
        }
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
        } Then {
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

        Given {
            contentType(ContentType.JSON)
        } When {
            log().all()
            get(PATIENT_REQUEST_URL)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
            assertThat().body("page.size", IsEqual.equalTo(10))
            assertThat().body("page.totalElements", IsEqual.equalTo(11))
            assertThat().body("page.totalPages", IsEqual.equalTo(2))
            assertThat().body("page.number", IsEqual.equalTo(0))
        }
    }

    @Test
    fun `환자 목록 조회 - 환자 이름 검색`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                val request = PatientCreateRequest(
                    hospitalId = 1L,
                    name = "${value}위 성현내과",
                    gender = "M",
                    birthDay = "1993-01-01",
                    mobilePhoneNumber = "010-1234-1234",
                )
                val savedPatient = patientService.createPatient(request)

                IntStream.range(0, 3).forEach {
                    val requestVisit = VisitCreateRequest(
                        hospitalId = 1,
                        visitStatus = "VISITING",
                        diagnosisType = "D"
                    )
                    visitService.createVisit(savedPatient.id, requestVisit)
                }
            }
        }

        Given {
            contentType(ContentType.JSON)
            queryParam("name", "3위 성현내과")
        } When {
            log().all()
            get(PATIENT_REQUEST_URL)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
            assertThat().body("page.size", IsEqual.equalTo(10))
            assertThat().body("page.totalElements", IsEqual.equalTo(1))
            assertThat().body("page.totalPages", IsEqual.equalTo(1))
            assertThat().body("page.number", IsEqual.equalTo(0))
            assertThat().body("_embedded.patientListingResponseList[0].name", equalTo("3위 성현내과"))
            assertThat().body("_embedded.patientListingResponseList[0].gender", equalTo("M"))
            assertThat().body(
                "_embedded.patientListingResponseList[0].birthDay",
                equalTo("1993-01-01")
            )
            assertThat().body(
                "_embedded.patientListingResponseList[0].mobilePhoneNumber",
                equalTo("010-1234-1234")
            )
            assertThat().body(
                "_embedded.patientListingResponseList[0].receptionDateTime",
                equalTo(LocalDate.now().toString())
            )
        }
    }

    @Test
    fun `환자 목록 조회 - 환자등록번호 검색`() {
        var expectedRegisterNo = ""
        IntStream.range(0, 11).forEach { value ->
            run {
                val request = PatientCreateRequest(
                    hospitalId = 1L,
                    name = "${value}위 성현내과",
                    gender = "M",
                    birthDay = "1993-01-01",
                    mobilePhoneNumber = "010-1234-1234",
                )
                val savedPatient = patientService.createPatient(request)

                if (value == 3) {
                    expectedRegisterNo = savedPatient.registerNo
                }

                IntStream.range(0, 3).forEach {
                    val requestVisit = VisitCreateRequest(
                        hospitalId = 1,
                        visitStatus = "VISITING",
                        diagnosisType = "D"
                    )
                    visitService.createVisit(savedPatient.id, requestVisit)
                }
            }
        }

        Given {
            contentType(ContentType.JSON)
            queryParam("registerNo", expectedRegisterNo)
        } When {
            log().all()
            get(PATIENT_REQUEST_URL)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
            assertThat().body("page.size", IsEqual.equalTo(10))
            assertThat().body("page.totalElements", IsEqual.equalTo(1))
            assertThat().body("page.totalPages", IsEqual.equalTo(1))
            assertThat().body("page.number", IsEqual.equalTo(0))
            assertThat().body("_embedded.patientListingResponseList[0].name", equalTo("3위 성현내과"))
            assertThat().body("_embedded.patientListingResponseList[0].gender", equalTo("M"))
            assertThat().body(
                "_embedded.patientListingResponseList[0].registerNo",
                equalTo(expectedRegisterNo)
            )
            assertThat().body(
                "_embedded.patientListingResponseList[0].birthDay",
                equalTo("1993-01-01")
            )
            assertThat().body(
                "_embedded.patientListingResponseList[0].mobilePhoneNumber",
                equalTo("010-1234-1234")
            )
            assertThat().body(
                "_embedded.patientListingResponseList[0].receptionDateTime",
                equalTo(LocalDate.now().toString())
            )
        }
    }

    @Test
    fun `환자 목록 조회 - 생년월일 검색`() {
        IntStream.range(20, 31).forEach { value ->
            run {
                val request = PatientCreateRequest(
                    hospitalId = 1L,
                    name = "${value}위 성현내과",
                    gender = "M",
                    birthDay = "1993-01-${value}",
                    mobilePhoneNumber = "010-1234-1234",
                )
                val savedPatient = patientService.createPatient(request)

                IntStream.range(0, 3).forEach {
                    val requestVisit = VisitCreateRequest(
                        hospitalId = 1,
                        visitStatus = "VISITING",
                        diagnosisType = "D"
                    )
                    visitService.createVisit(savedPatient.id, requestVisit)
                }
            }
        }

        Given {
            contentType(ContentType.JSON)
            queryParam("birthDay", "1993-01-22")
        } When {
            log().all()
            get(PATIENT_REQUEST_URL)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
            assertThat().body("page.size", IsEqual.equalTo(10))
            assertThat().body("page.totalElements", IsEqual.equalTo(1))
            assertThat().body("page.totalPages", IsEqual.equalTo(1))
            assertThat().body("page.number", IsEqual.equalTo(0))
            assertThat().body("_embedded.patientListingResponseList[0].name", equalTo("22위 성현내과"))
            assertThat().body("_embedded.patientListingResponseList[0].gender", equalTo("M"))
            assertThat().body(
                "_embedded.patientListingResponseList[0].birthDay",
                equalTo("1993-01-22")
            )
            assertThat().body(
                "_embedded.patientListingResponseList[0].mobilePhoneNumber",
                equalTo("010-1234-1234")
            )
            assertThat().body(
                "_embedded.patientListingResponseList[0].receptionDateTime",
                equalTo(LocalDate.now().toString())
            )
        }
    }

    @Test
    fun `환자 목록 조회 - 페이지사이즈 5`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                val request = PatientCreateRequest(
                    hospitalId = 1L,
                    name = "${value}위 성현내과",
                    gender = "M",
                    birthDay = "1993-01-01",
                    mobilePhoneNumber = "010-1234-1234",
                )
                val savedPatient = patientService.createPatient(request)

                IntStream.range(0, 3).forEach {
                    val requestVisit = VisitCreateRequest(
                        hospitalId = 1,
                        visitStatus = "VISITING",
                        diagnosisType = "D"
                    )
                    visitService.createVisit(savedPatient.id, requestVisit)
                }
            }
        }

        Given {
            contentType(ContentType.JSON)
            queryParam("size", 5)
        } When {
            log().all()
            get(PATIENT_REQUEST_URL)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
            assertThat().body("page.size", IsEqual.equalTo(5))
            assertThat().body("page.totalElements", IsEqual.equalTo(11))
            assertThat().body("page.totalPages", IsEqual.equalTo(3))
            assertThat().body("page.number", IsEqual.equalTo(0))
        }
    }

    @Test
    fun `환자 목록 조회 - 페이지사이즈 8`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                val request = PatientCreateRequest(
                    hospitalId = 1L,
                    name = "${value}위 성현내과",
                    gender = "M",
                    birthDay = "1993-01-01",
                    mobilePhoneNumber = "010-1234-1234",
                )
                val savedPatient = patientService.createPatient(request)

                IntStream.range(0, 3).forEach {
                    val requestVisit = VisitCreateRequest(
                        hospitalId = 1,
                        visitStatus = "VISITING",
                        diagnosisType = "D"
                    )
                    visitService.createVisit(savedPatient.id, requestVisit)
                }
            }
        }

        Given {
            contentType(ContentType.JSON)
            queryParam("size", 8)
        } When {
            log().all()
            get(PATIENT_REQUEST_URL)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
            assertThat().body("page.size", IsEqual.equalTo(8))
            assertThat().body("page.totalElements", IsEqual.equalTo(11))
            assertThat().body("page.totalPages", IsEqual.equalTo(2))
            assertThat().body("page.number", IsEqual.equalTo(0))
        }
    }

    @Test
    fun `환자 목록 조회 - 페이지넘버 0`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                val request = PatientCreateRequest(
                    hospitalId = 1L,
                    name = "${value}위 성현내과",
                    gender = "M",
                    birthDay = "1993-01-01",
                    mobilePhoneNumber = "010-1234-1234",
                )
                val savedPatient = patientService.createPatient(request)

                IntStream.range(0, 3).forEach {
                    val requestVisit = VisitCreateRequest(
                        hospitalId = 1,
                        visitStatus = "VISITING",
                        diagnosisType = "D"
                    )
                    visitService.createVisit(savedPatient.id, requestVisit)
                }
            }
        }

        Given {
            contentType(ContentType.JSON)
            queryParam("number", 0)
        } When {
            log().all()
            get(PATIENT_REQUEST_URL)
        } Then {
            log().all()
            statusCode(HttpStatus.OK.value())
            assertThat().body("page.size", IsEqual.equalTo(10))
            assertThat().body("page.totalElements", IsEqual.equalTo(11))
            assertThat().body("page.totalPages", IsEqual.equalTo(2))
            assertThat().body("page.number", IsEqual.equalTo(0))
        }
    }

}