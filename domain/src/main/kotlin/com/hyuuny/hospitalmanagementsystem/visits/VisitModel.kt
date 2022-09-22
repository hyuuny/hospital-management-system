package com.hyuuny.hospitalmanagementsystem.visits

import com.fasterxml.jackson.annotation.JsonFormat
import com.hyuuny.hospitalmanagementsystem.patients.Patient
import java.time.LocalDateTime

data class VisitCreateRequest(
    val hospitalId: Long,
    val visitStatus: String,
    val diagnosisType: String,
) {
    fun toEntity(patient: Patient): Visit = Visit.create(
        hospitalId = this.hospitalId,
        patient = patient,
        visitStatus = this.visitStatus,
        diagnosisType = this.diagnosisType,
    )
}

data class VisitUpdateRequest(
    val visitStatus: String,
    val diagnosisType: String,
    val receptionDateTime: LocalDateTime,
) {
    fun update(visit: Visit) {
        visit.changeVisitStatus(this.visitStatus)
        visit.changeDiagnosisType(this.diagnosisType)
        visit.changeReceptionDateTime(this.receptionDateTime)
    }
}

data class VisitResponse(
    val id: Long,
    val hospitalId: Long,
    val patientId: Long,
    val visitStatus: VisitStatus,
    val diagnosisType: DiagnosisType,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val receptionDateTime: LocalDateTime,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val lastModifiedAt: LocalDateTime,
) {
    companion object {
        operator fun invoke(visit: Visit) = with(visit) {
            VisitResponse(
                id = id!!,
                hospitalId = hospitalId,
                patientId = patient.id!!,
                visitStatus = visitStatus,
                diagnosisType = diagnosisType,
                receptionDateTime = receptionDateTime,
                createdAt = createdAt,
                lastModifiedAt = lastModifiedAt,
            )
        }
    }
}
