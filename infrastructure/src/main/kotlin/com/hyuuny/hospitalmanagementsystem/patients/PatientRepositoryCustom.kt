package com.hyuuny.hospitalmanagementsystem.patients

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface PatientRepositoryCustom {

    fun retrievePatients(
        searchCondition: PatientSearchCondition,
        pageable: Pageable,
    ) : PageImpl<SearchedPatientListing>

}