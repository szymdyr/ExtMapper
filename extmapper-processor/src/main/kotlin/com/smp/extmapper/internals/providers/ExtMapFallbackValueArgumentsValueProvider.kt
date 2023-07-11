package com.smp.extmapper.internals.providers

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.smp.extmapper.annotations.ExtMapFallbackValue
import com.smp.extmapper.internals.extensions.getArgument

internal interface ExtMapFallbackValueArgumentsValueProvider {
    fun getForceUnwrapValue(symbol: KSAnnotated): Boolean

    fun getFallbackValueProvider(symbol: KSAnnotated): KSClassDeclaration
}

internal class ExtMapFallbackValueArgumentsValueProviderImpl :
    ExtMapFallbackValueArgumentsValueProvider {
    private val annotationProvider: AnnotationProvider = AnnotationProviderImpl()

    override fun getForceUnwrapValue(symbol: KSAnnotated): Boolean =
        annotationProvider.provide<ExtMapFallbackValue>(symbol)
            .getArgument(ExtMapFallbackValue::forceUnwrap.name)
            .run { value as Boolean }

    override fun getFallbackValueProvider(symbol: KSAnnotated): KSClassDeclaration =
        annotationProvider.provide<ExtMapFallbackValue>(symbol)
            .getArgument(ExtMapFallbackValue::fallbackValueProvider.name)
            .run { value as KSType }
            .run { declaration as KSClassDeclaration }

}