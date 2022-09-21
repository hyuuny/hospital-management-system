package com.hyuuny.hospitalmanagementsystem.patients

import org.springframework.data.jpa.repository.JpaRepository

interface PatientRepository : JpaRepository<Patient, Long> {
}