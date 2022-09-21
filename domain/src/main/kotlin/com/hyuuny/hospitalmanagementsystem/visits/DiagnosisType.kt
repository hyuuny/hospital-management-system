package com.hyuuny.hospitalmanagementsystem.visits

enum class DiagnosisType(private val title: String) {

    D("약처방"),
    T("검사");

    companion object {
        operator fun invoke(type: String) = valueOf(type.uppercase())
    }

}