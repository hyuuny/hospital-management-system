package com.hyuuny.hospitalmanagementsystem.visits

import com.hyuuny.hospitalmanagementsystem.patients.Patient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VisitTest {

    @Test
    fun `환자방문 객체 생성`() {
        val expectedHospitalId = 1L
        val expectedPatient = createPatient()
        val expectedVisitStatus = "VISITING"
        val expectedDiagnosisType = "D"

        val newVisit = createVisit(patient = expectedPatient)
        assertThat(newVisit.hospitalId).isEqualTo(expectedHospitalId)
        assertThat(newVisit.patient).isEqualTo(expectedPatient)
        assertThat(newVisit.visitStatus.name).isEqualTo(expectedVisitStatus)
        assertThat(newVisit.diagnosisType.name).isEqualTo(expectedDiagnosisType)
        assertThat(newVisit.receptionDateTime).isNotNull
    }

    @Test
    fun `방문상태코드 수정`() {
        val visitStatus = "VISITING"
        val updatedVisitStatus = "CANCELLATION"
        val newVisit = createVisit()
        assertThat(newVisit.visitStatus.name).isEqualTo(visitStatus)

        newVisit.changeVisitStatus(updatedVisitStatus)
        assertThat(newVisit.visitStatus.name).isEqualTo(updatedVisitStatus)
    }

    private fun createVisit(
        hospitalId: Long = 1L,
        patient: Patient = createPatient(),
        visitStatus: String = "VISITING",
        diagnosisType: String = "D",
        receptionDateTime: LocalDateTime = LocalDateTime.now(),
    ): Visit = Visit.create(
        hospitalId = hospitalId,
        patient = patient,
        visitStatus = visitStatus,
        diagnosisType = diagnosisType,
        receptionDateTime = receptionDateTime
    )

    private fun createPatient(
        hospitalId: Long = 1,
        name: String = "김환자",
        gender: String = "M",
        brithDay: String = "1993-01-01",
        mobilePhoneNumber: String = "010-1324-1234",
        visits: MutableList<Visit> = mutableListOf(),
    ): Patient = Patient.create(
        hospitalId = hospitalId,
        name = name,
        gender = gender,
        birthDay = brithDay,
        mobilePhoneNumber = mobilePhoneNumber,
        visits = visits,
    )

}