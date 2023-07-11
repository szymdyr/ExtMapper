package com.smp.extmapper.internals.providers

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import kotlin.reflect.KClass

internal interface AnnotationProvider {
    fun <T : Annotation> provide(
        symbol: KSAnnotated,
        annotationType: KClass<T>
    ): KSAnnotation

    fun <T : Annotation> contains(
        symbol: KSAnnotated,
        annotationType: KClass<T>
    ): Boolean
}

internal class AnnotationProviderImpl : AnnotationProvider {
    override fun <T : Annotation> provide(
        symbol: KSAnnotated,
        annotationType: KClass<T>
    ): KSAnnotation =
        symbol.annotations
            .first { it.shortName.asString() == annotationType.simpleName }

    override fun <T : Annotation> contains(
        symbol: KSAnnotated,
        annotationType: KClass<T>
    ): Boolean =
        symbol.annotations
            .any { it.shortName.asString() == annotationType.simpleName }

}

internal inline fun <reified T : Annotation> AnnotationProvider.provide(symbol: KSAnnotated) =
    provide(symbol, T::class)

internal inline fun <reified T : Annotation> AnnotationProvider.contains(symbol: KSAnnotated) =
    contains(symbol, T::class)