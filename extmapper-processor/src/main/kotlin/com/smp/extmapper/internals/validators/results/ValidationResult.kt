package com.smp.extmapper.internals.validators.results

internal sealed class ValidationResult {
    object ValidationSuccess : ValidationResult()

    data class ValidationError<T>(val error: T) : ValidationResult()
}