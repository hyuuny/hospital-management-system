package com.hyuuny.hospitalmanagementsystem.patients

enum class Gender(private val title: String) {

    M("님"),
    F("여");

    companion object {
        operator fun invoke(gender: String) = valueOf(gender.uppercase())
    }

}