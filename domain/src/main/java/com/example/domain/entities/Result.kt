package com.example.domain.entities

/**
 * Result Wrapper Class to encapsulate different data state
 * reference:https://github.com/android/nowinandroid?tab=readme-ov-file
 */
sealed class Result<T: Any> {
    data class Success<T: Any>(val data : T) : Result<T>()
    data class Error(val errorMessage : String?) : Result<Nothing>()
}