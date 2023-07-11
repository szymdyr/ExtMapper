package com.smp.extmapper.internals.validators

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.smp.extmapper.internals.providers.ExtMapFromArgumentsValueProvider
import com.smp.extmapper.internals.providers.ExtMapFromArgumentsValueProviderImpl

internal interface ExtMapFromSymbolAnnotatedValidator : SymbolValidator

internal class ExtMapFromSymbolAnnotatedValidatorImpl: ExtMapFromSymbolAnnotatedValidator {
    private val annotationValueProvider: ExtMapFromArgumentsValueProvider =
        ExtMapFromArgumentsValueProviderImpl()

    private val visibilityModifierValueValidator: VisibilityModifierValueValidator =
        VisibilityModifierValueValidatorImpl()

    override fun isValid(symbol: KSAnnotated): Boolean =
        symbol is KSClassDeclaration &&
                symbol.validate() &&
                symbol.hasValidFromParameter() &&
                symbol.hasValidVisibilityModifier()

    private fun KSClassDeclaration.hasValidFromParameter(): Boolean {
        val fromClass = annotationValueProvider.getFromClassValue(this)

        return fromClass.classKind == classKind && //same class kinds
                (fromClass.classKind == ClassKind.CLASS ||
                        fromClass.classKind == ClassKind.ENUM_CLASS)
    }

    private fun KSClassDeclaration.hasValidVisibilityModifier(): Boolean =
        visibilityModifierValueValidator.isValid(
            annotationValueProvider.getVisibilityModifierValue(this)
        )
}
