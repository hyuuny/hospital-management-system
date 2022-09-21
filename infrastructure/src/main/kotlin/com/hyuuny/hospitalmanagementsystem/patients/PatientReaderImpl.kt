package com.hyuuny.hospitalmanagementsystem.patients

import com.hyuuny.hospitalmanagementsystem.exception.NotFountException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class PatientReaderImpl(
    private val patientRepository: PatientRepository,
) : PatientReader{

    override fun getPatient(id: Long): Patient =
        patientRepository.findByIdOrNull(id) ?: throw NotFountException("환자를 찾을 수 없습니다.")

}