package com.hyuuny.hospitalmanagementsystem.visits

interface VisitReader {

    fun getVisit(id: Long): Visit

}