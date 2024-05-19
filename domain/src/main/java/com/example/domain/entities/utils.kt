package com.example.domain.entities

enum class Window(s: String) {
    DAY("day"), WEEK("week")
}

enum class Locale(s: String) {
    EN_US("en-US")
}

enum class ErrorType {
    IO, HTTP, MALFORMED, OTHER
}