package com.hyuuny.hospitalmanagementsystem.visits

import com.hyuuny.hospitalmanagementsystem.patients.PatientReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class VisitService(
    private val visitStore: VisitStore,
    private val visitReader: VisitReader,
    private val patientReader: PatientReader,
) {

    @Transactional
    fun createVisit(
        patientId: Long,
        request: VisitCreateRequest
    ): VisitResponse {
        val foundPatient = patientReader.getPatient(patientId)
        val newVisit = request.toEntity(foundPatient)
        foundPatient.visits!!.add(newVisit)
        return VisitResponse(visitStore.store(newVisit))
    }

    fun getVisit(id: Long): VisitResponse {
        val foundVisit = visitReader.getVisit(id)
        return VisitResponse(foundVisit)
    }

    @Transactional
    fun updateVisit(id: Long, request: VisitUpdateRequest): VisitResponse {
        val foundVisit = visitReader.getVisit(id)
        request.update(foundVisit)
        return getVisit(id)
    }

    @Transactional
    fun deleteVisit(id: Long) = visitStore.delete(id)

}