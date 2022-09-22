package com.hyuuny.hospitalmanagementsystem.patients

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PatientService(
    private val patientStore: PatientStore,
    private val patientReader: PatientReader,
) {

    @Transactional
    fun createPatient(request: PatientCreateRequest): PatientResponse {
        val newPatient = request.toEntity()
        return PatientResponse(patientStore.store(newPatient))
    }

    fun getPatient(id: Long): PatientResponse {
        val foundPatient = patientReader.getPatient(id)
        return PatientResponse(foundPatient)
    }

    @Transactional
    fun updatePatient(id: Long, request: PatientUpdateRequest): PatientResponse {
        val foundPatient = patientReader.getPatient(id)
        request.update(foundPatient)
        return getPatient(id)
    }

    @Transactional
    fun deletePatient(id: Long) = patientStore.delete(id)

    fun retrievePatients(
        searchCondition: PatientSearchCondition,
        pageable: Pageable,
    ): PageImpl<PatientListingResponse> {
        val searched = patientReader.retrievePatients(searchCondition, pageable)
        val searchedPatients = SearchedPatients(searched.content)
        return PageImpl(searchedPatients.toPage(), pageable, searched.totalElements)
    }

}