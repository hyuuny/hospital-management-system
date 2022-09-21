package com.hyuuny.hospitalmanagementsystem.patients

import com.hyuuny.hospitalmanagementsystem.visits.Visit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PatientTest {

    @Test
    fun `환자 객체 생성`() {
        val expectedHospitalId = 1L
        val expectedName = "김환자"
        val expectedGender = "M"
        val expectedBirthDay = "1993-01-01"
        val expectedMobilePhoneNumber = "010-1324-1234"

        val newPatient = createPatient()
        assertThat(newPatient.hospitalId).isEqualTo(expectedHospitalId)
        assertThat(newPatient.name).isEqualTo(expectedName)
        assertThat(newPatient.gender.name).isEqualTo(expectedGender)
        assertThat(newPatient.birthDay).isEqualTo(expectedBirthDay)
        assertThat(newPatient.mobilePhoneNumber).isEqualTo(expectedMobilePhoneNumber)
        assertThat(newPatient.visits).isEmpty()
        assertThat(newPatient.registerNo).isNotNull
    }

    @Test
    fun `환자 이름 수정`() {
        val name = "김철수"
        val updatedName = "나환자"
        val newPatient = createPatient(name = name)
        assertThat(newPatient.name).isEqualTo(name)

        newPatient.changeName(updatedName)
        assertThat(newPatient.name).isEqualTo(updatedName)
    }

    @Test
    fun `생년월일 수정`() {
        val brithDay = "1993-04-19"
        val updatedBirthDay = "2000-01-01"
        val newPatient = createPatient(brithDay = brithDay)
        assertThat(newPatient.birthDay).isEqualTo(brithDay)

        newPatient.changeBirthDay(updatedBirthDay)
        assertThat(newPatient.birthDay).isEqualTo(updatedBirthDay)
    }

    @Test
    fun `성별 수정`() {
        val gender = "M"
        val updatedGender = "F"
        val newPatient = createPatient(gender = gender)
        assertThat(newPatient.gender.name).isEqualTo(gender)

        newPatient.changeGender(updatedGender)
        assertThat(newPatient.gender.name).isEqualTo(updatedGender)
    }

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