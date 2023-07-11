package com.smp.extmapper.internals.providers

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.smp.extmapper.annotations.ExtMapTo
import com.smp.extmapper.internals.extensions.getArgument

internal interface ExtMapToArgumentsValueProvider {
    fun getToClassValue(symbol: KSAnnotated): KSClassDeclaration
}

internal class ExtMapToArgumentsValueProviderImpl: ExtMapToArgumentsValueProvider {
    private val annotationProvider: AnnotationProvider = AnnotationProviderImpl()

    override fun getToClassValue(symbol: KSAnnotated) =
        annotationProvider.provide<ExtMapTo>(symbol)
            .getArgument(ExtMapTo::toClass.name)
            .run { value as KSType }
            .run { declaration as KSClassDeclaration }
}