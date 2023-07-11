package com.smp.extmapper.internals.factories

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.smp.extmapper.annotations.ExtMapPropertyName
import com.smp.extmapper.internals.extensions.toCodeBlock
import com.smp.extmapper.internals.providers.AnnotationProvider
import com.smp.extmapper.internals.providers.AnnotationProviderImpl
import com.smp.extmapper.internals.providers.ExtMapFallbackValueArgumentsValueProvider
import com.smp.extmapper.internals.providers.ExtMapFallbackValueArgumentsValueProviderImpl
import com.smp.extmapper.internals.providers.ExtMapPropertyNameArgumentsValueProvider
import com.smp.extmapper.internals.providers.ExtMapPropertyNameArgumentsValueProviderImpl
import com.smp.extmapper.internals.providers.contains
import com.smp.extmapper.internals.validators.PropertiesValidator
import com.smp.extmapper.internals.validators.PropertiesValidator.PropertiesValidationError
import com.smp.extmapper.internals.validators.PropertiesValidatorImpl
import com.smp.extmapper.internals.validators.results.ValidationResult
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ksp.toClassName

internal interface PropertyNameFactory {
    fun create(annotatedProperty: KSPropertyDeclaration, otherClass: KSClassDeclaration): CodeBlock
}

@Suppress("UNCHECKED_CAST")
internal class PropertyNameFactoryImpl : PropertyNameFactory {
    private val annotationProvider: AnnotationProvider = AnnotationProviderImpl()
    private val propertyNameArgumentsValueProvider: ExtMapPropertyNameArgumentsValueProvider =
        ExtMapPropertyNameArgumentsValueProviderImpl()
    private val fallbackValueArgumentsValueProvider: ExtMapFallbackValueArgumentsValueProvider =
        ExtMapFallbackValueArgumentsValueProviderImpl()
    private val propertiesValidator: PropertiesValidator = PropertiesValidatorImpl()

    //TODO - add support for alternative names
    override fun create(
        annotatedProperty: KSPropertyDeclaration,
        otherClass: KSClassDeclaration
    ): CodeBlock {
        //find corresponding property
        val propertyName = if (annotationProvider.contains<ExtMapPropertyName>(annotatedProperty)) {
            propertyNameArgumentsValueProvider.getNameValue(annotatedProperty)
        } else {
            annotatedProperty.simpleName.asString()
        }

        val propertyDeclaration = otherClass.getDeclaredProperties().first {
            it.simpleName.asString() == propertyName
        }

        return when (
            val validationResult = propertiesValidator.haveValidType(
                annotatedProperty,
                propertyDeclaration
            )
        ) {
            //types are valid - we can return the name of property
            is ValidationResult.ValidationSuccess -> propertyName.toCodeBlock()

            //need to handle fallback
            is ValidationResult.ValidationError<*> -> handleError(
                annotatedProperty,
                propertyName,
                validationResult as ValidationResult.ValidationError<PropertiesValidationError>
            )
        }
    }

    private fun handleError(
        annotatedProperty: KSPropertyDeclaration,
        propertyName: String,
        validationResult: ValidationResult.ValidationError<PropertiesValidationError>
    ): CodeBlock =
        when (validationResult.error) {
            PropertiesValidationError.NOT_NULLABLE -> handleNotNullableCase(
                annotatedProperty,
                propertyName
            )
        }

    private fun handleNotNullableCase(
        annotatedProperty: KSPropertyDeclaration,
        propertyName: String,
    ): CodeBlock =
        //if forceUnwrap is requested then add !! operator
        if (fallbackValueArgumentsValueProvider.getForceUnwrapValue(annotatedProperty)) {
            "$propertyName!!".toCodeBlock()
        } else {
            //TODO: check if types for fallbackProvider are ok
            val fallbackProvider =
                fallbackValueArgumentsValueProvider.getFallbackValueProvider(annotatedProperty)

            "$propertyName ?: %T().provide(this)".toCodeBlock(fallbackProvider.toClassName())
        }
}
