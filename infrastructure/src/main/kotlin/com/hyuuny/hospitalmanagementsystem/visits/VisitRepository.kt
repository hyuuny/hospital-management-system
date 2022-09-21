package com.hyuuny.hospitalmanagementsystem.visits

import org.springframework.data.jpa.repository.JpaRepository

interface VisitRepository : JpaRepository<Visit, Long> {
}