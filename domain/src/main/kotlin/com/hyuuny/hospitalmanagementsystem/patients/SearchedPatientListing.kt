package com.hyuuny.hospitalmanagementsystem.patients

import java.time.LocalDateTime

data class SearchedPatientListing(
    val id: Long? = null,
    val hospitalId: Long? = null,
    val name: String? = null,
    val registerNo: String? = null,
    val gender: Gender? = null,
    val birthDay: String? = null,
    val mobilePhoneNumber: String? = null,
    val receptionDateTime: LocalDateTime? = null,
)
