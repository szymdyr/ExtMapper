package com.smp.extmapper.internals.validators

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.smp.extmapper.internals.validators.results.ValidationResult

internal interface PropertiesValidator {

    fun haveValidType(
        toProperty: KSPropertyDeclaration,
        fromProperty: KSPropertyDeclaration
    ): ValidationResult

    enum class PropertiesValidationError {
        NOT_NULLABLE
    }
}

internal class PropertiesValidatorImpl : PropertiesValidator {
    override fun haveValidType(
        toProperty: KSPropertyDeclaration,
        fromProperty: KSPropertyDeclaration
    ): ValidationResult =
        toProperty hasValidNullConditionWith fromProperty

    /**
     * Check if mapping between properties can be performed based on nullability.
     * If we are mapping from non nullable property then we are good. If we are mapping from
     * nullable type to nullable then we are good. If destination property is non nullable then
     * we have an error.
     */
    private infix fun KSPropertyDeclaration.hasValidNullConditionWith(
        fromProperty: KSPropertyDeclaration
    ): ValidationResult {
        val fromPropertyType = fromProperty.type.resolve()
        if (!fromPropertyType.isMarkedNullable) return ValidationResult.ValidationSuccess

        val toPropertyType = this.type.resolve()
        return if (toPropertyType.isMarkedNullable)
            ValidationResult.ValidationSuccess
        else
            ValidationResult.ValidationError(
                PropertiesValidator.PropertiesValidationError.NOT_NULLABLE
            )
    }
}