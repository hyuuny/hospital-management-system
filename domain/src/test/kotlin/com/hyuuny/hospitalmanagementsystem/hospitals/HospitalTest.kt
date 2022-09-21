package com.hyuuny.hospitalmanagementsystem.hospitals

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HospitalTest {


    @Test
    fun `병원 객체 생성`() {
        val expectedName = "성현내과"
        val expectedCode = "11100082"
        val expectedHospitalDirectorName = "김성현"
        val create = createHospital()

        assertThat(create.name).isEqualTo(expectedName)
        assertThat(create.code).isEqualTo(expectedCode)
        assertThat(create.hospitalDirectorName).isEqualTo(expectedHospitalDirectorName)
    }

    @Test
    fun `병원 이름 수정`() {
        val name = "태양병원"
        val updatedName = "달병원"
        val newHospital = createHospital(name = name)
        assertThat(newHospital.name).isEqualTo(name)

        newHospital.changeName(updatedName)
        assertThat(newHospital.name).isEqualTo(updatedName)
    }

    private fun createHospital(
        name: String = "성현내과",
        code: String = "11100082",
        hospitalDirectorName: String = "김성현",
    ): Hospital = Hospital.create(
        name = name,
        code = code,
        hospitalDirectorName = hospitalDirectorName,
    )

}