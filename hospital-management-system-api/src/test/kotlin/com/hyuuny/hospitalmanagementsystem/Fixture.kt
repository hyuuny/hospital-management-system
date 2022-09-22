package com.hyuuny.hospitalmanagementsystem

import com.hyuuny.hospitalmanagementsystem.patients.PatientCreateRequest
import com.hyuuny.hospitalmanagementsystem.visits.VisitCreateRequest

class FixturePatient {
    companion object {
        fun aPatientCreateRequest(): PatientCreateRequest = PatientCreateRequest(
            hospitalId = 1L,
            name = "성현내과",
            gender = "M",
            birthDay = "1993-01-01",
            mobilePhoneNumber = "010-1234-1234",
        )
    }
}

class FixtureVisit {
    companion object {
        fun aVisitCreateRequest() = VisitCreateRequest(
            hospitalId = 1,
            visitStatus = "VISITING",
            diagnosisType = "D"
        )
    }
}