package com.smp.extmapper.internals.providers

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.smp.extmapper.annotations.ExtMapFrom
import com.smp.extmapper.internals.extensions.getArgument

internal interface ExtMapFromArgumentsValueProvider {
    fun getFromClassValue(symbol: KSAnnotated): KSClassDeclaration

    fun getVisibilityModifierValue(symbol: KSAnnotated): Int
}

internal class ExtMapFromArgumentsValueProviderImpl: ExtMapFromArgumentsValueProvider {
    private val annotationProvider: AnnotationProvider = AnnotationProviderImpl()

    override fun getFromClassValue(symbol: KSAnnotated) =
        annotationProvider.provide<ExtMapFrom>(symbol)
            .getArgument(ExtMapFrom::fromClass.name)
            .run { value as KSType }
            .run { declaration as KSClassDeclaration }

    override fun getVisibilityModifierValue(symbol: KSAnnotated): Int =
        annotationProvider.provide<ExtMapFrom>(symbol)
            .getArgument(ExtMapFrom::visibilityModifier.name)
            .run { value as Int }
}