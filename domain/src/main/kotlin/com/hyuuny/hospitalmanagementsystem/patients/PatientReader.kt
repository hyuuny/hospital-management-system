package com.hyuuny.hospitalmanagementsystem.patients

interface PatientReader {

    fun getPatient(id: Long): Patient

}