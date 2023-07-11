package com.smp.extmapper.internals.providers

import com.google.devtools.ksp.symbol.KSAnnotated
import com.smp.extmapper.annotations.ExtMapPropertyName
import com.smp.extmapper.internals.extensions.getArgument

internal interface ExtMapPropertyNameArgumentsValueProvider {
    fun getNameValue(symbol: KSAnnotated): String

    fun getAlternativeNamesValue(symbol: KSAnnotated): List<String>
}

internal class ExtMapPropertyNameArgumentsValueProviderImpl :
    ExtMapPropertyNameArgumentsValueProvider {
    private val annotationProvider: AnnotationProvider = AnnotationProviderImpl()

    override fun getNameValue(symbol: KSAnnotated): String =
        annotationProvider.provide<ExtMapPropertyName>(symbol)
            .getArgument(ExtMapPropertyName::name.name)
            .run { value as String }

    override fun getAlternativeNamesValue(symbol: KSAnnotated): List<String> {
        TODO("Not yet implemented")
    }

}