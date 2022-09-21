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