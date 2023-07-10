package com.smp.extmapper.internals.providers

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.reflect.KClass

internal interface AnnotationProvider {
    fun <T : Annotation> provide(
        annotatedClass: KSClassDeclaration,
        annotationType: KClass<T>
    ): KSAnnotation
}

internal class AnnotationProviderImpl : AnnotationProvider {
    override fun <T : Annotation> provide(
        annotatedClass: KSClassDeclaration,
        annotationType: KClass<T>
    ): KSAnnotation =
        annotatedClass.annotations
            .first { it.shortName.asString() == annotationType.simpleName }

}

internal inline fun <reified T : Annotation> AnnotationProvider.provide(
    annotatedClass: KSClassDeclaration
) = provide(annotatedClass, T::class)