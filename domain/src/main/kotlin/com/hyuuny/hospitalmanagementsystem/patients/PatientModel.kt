package com.hyuuny.hospitalmanagementsystem.patients

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.hospitalmanagementsystem.visits.Visit
import com.hyuuny.hospitalmanagementsystem.visits.VisitResponse
import java.time.LocalDateTime

data class PatientCreateRequest(
    val hospitalId: Long,
    val name: String,
    val gender: String,
    val birthDay: String? = null,
    val mobilePhoneNumber: String? = null,
) {
    fun toEntity(): Patient = Patient.create(
        hospitalId = this.hospitalId,
        name = this.name,
        gender = this.gender,
        birthDay = this.birthDay,
        mobilePhoneNumber = this.mobilePhoneNumber,
    )
}

data class PatientUpdateRequest(
    val name: String,
    val gender: String,
    val birthDay: String? = null,
    val mobilePhoneNumber: String? = null
) {
    fun update(patient: Patient) {
        patient.changeName(this.name)
        patient.changeGender(this.gender)
        patient.changeBirthDay(this.birthDay)
        patient.changeMobilePhoneNumber(this.mobilePhoneNumber)
    }
}

data class PatientResponse(
    val id: Long,
    val hospitalId: Long,
    val name: String,
    val registerNo: String,
    val gender: Gender,
    val birthDay: String? = null,
    val mobilePhoneNumber: String? = null,
    val visits: List<VisitResponse> = emptyList(),

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val lastModifiedAt: LocalDateTime,
) {
    companion object {
        operator fun invoke(patient: Patient) = with(patient) {
            PatientResponse(
                id = id!!,
                hospitalId = hospitalId,
                name = name,
                registerNo = registerNo,
                gender = gender,
                birthDay = birthDay,
                mobilePhoneNumber = mobilePhoneNumber,
                visits = visits?.sortedByDescending(Visit::id)!!.map { VisitResponse(it) },
                createdAt = createdAt,
                lastModifiedAt = lastModifiedAt,
            )
        }
    }
}

@JsonInclude(Include.NON_NULL)
data class PatientListingResponse(
    val id: Long,
    val hospitalId: Long,
    val name: String,
    val registerNo: String,
    val gender: Gender,
    val birthDay: String?,
    val mobilePhoneNumber: String?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val receptionDateTime: LocalDateTime?,
) {
    companion object {
        operator fun invoke(searchedPatientListing: SearchedPatientListing) =
            with(searchedPatientListing) {
                PatientListingResponse(
                    id = id!!,
                    hospitalId = hospitalId!!,
                    name = name!!,
                    registerNo = registerNo!!,
                    gender = gender!!,
                    birthDay = birthDay,
                    mobilePhoneNumber = mobilePhoneNumber,
                    receptionDateTime = receptionDateTime,
                )
            }
    }
}

