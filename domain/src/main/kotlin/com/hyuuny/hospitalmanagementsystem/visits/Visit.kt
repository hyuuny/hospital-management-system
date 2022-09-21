package com.hyuuny.hospitalmanagementsystem.visits

import com.hyuuny.hospitalmanagementsystem.domain.BaseEntity
import com.hyuuny.hospitalmanagementsystem.patients.Patient
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Visit private constructor(

    @Column(nullable = false)
    val hospitalId: Long,

    @Column(unique = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    val patient: Patient,

    visitStatus: VisitStatus,

    diagnosisType: DiagnosisType,

    receptionDateTime: LocalDateTime,
) : BaseEntity() {
    companion object {
        fun create(
            hospitalId: Long,
            patient: Patient,
            visitStatus: String = "VISITING",
            diagnosisType: String,
            receptionDateTime: LocalDateTime = LocalDateTime.now(),
        ): Visit = Visit(
            hospitalId = hospitalId,
            patient = patient,
            visitStatus = VisitStatus(visitStatus),
            diagnosisType = DiagnosisType(diagnosisType),
            receptionDateTime = receptionDateTime
        )
    }

    @Column(nullable = false)
    var visitStatus = visitStatus
        private set

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var diagnosisType = diagnosisType
        private set

    @Column(nullable = false, length = 10)
    var receptionDateTime = receptionDateTime
        private set

    fun changeVisitStatus(visitStatus: String) {
        this.visitStatus = VisitStatus(visitStatus)
    }

    fun changeDiagnosisType(diagnosisType: String) {
        this.diagnosisType = DiagnosisType(diagnosisType)
    }

    fun changeReceptionDateTime(receptionDateTime: LocalDateTime) {
        this.receptionDateTime = receptionDateTime
    }

}