package com.hyuuny.hospitalmanagementsystem.patients

interface PatientStore {

    fun store(patient: Patient): Patient

    fun delete(id: Long)

}