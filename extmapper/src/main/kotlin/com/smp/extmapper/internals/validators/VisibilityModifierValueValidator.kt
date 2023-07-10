package com.smp.extmapper.internals.validators

internal interface VisibilityModifierValueValidator {
    fun isValid(value: Int): Boolean
}

internal class VisibilityModifierValueValidatorImpl: VisibilityModifierValueValidator {
    override fun isValid(value: Int): Boolean = value in (MIN_VALUE..MAX_VALUE)

    private companion object {
        const val MIN_VALUE = 0
        const val MAX_VALUE = 1
    }
}