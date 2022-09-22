package com.hyuuny.hospitalmanagementsystem.patients

import org.springframework.stereotype.Component

@Component
class PatientStoreImpl(
    private val patientRepository: PatientRepository,
) : PatientStore {

    override fun store(patient: Patient): Patient = patientRepository.save(patient)

    override fun delete(id: Long) = patientRepository.deleteById(id)


}