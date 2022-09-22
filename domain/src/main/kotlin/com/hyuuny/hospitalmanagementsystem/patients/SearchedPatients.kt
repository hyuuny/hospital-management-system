package com.hyuuny.hospitalmanagementsystem.patients

data class SearchedPatients(
    val patients: List<SearchedPatientListing>,
) {
    fun toPage(): List<PatientListingResponse> = this.patients
        .map { PatientListingResponse(it) }
        .toList()
}
