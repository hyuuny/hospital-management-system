package com.hyuuny.hospitalmanagementsystem.patients

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface PatientReader {

    fun getPatient(id: Long): Patient

    fun retrievePatient(pageable: Pageable): PageImpl<SearchedPatientListing>

}