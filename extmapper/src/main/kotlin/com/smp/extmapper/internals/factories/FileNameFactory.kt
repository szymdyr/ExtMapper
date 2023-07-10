package com.smp.extmapper.internals.factories

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.smp.extmapper.annotations.ExtMapFrom
import com.smp.extmapper.annotations.ExtMapTo
import com.smp.extmapper.internals.MapDirection
import com.smp.extmapper.internals.providers.AnnotationProvider
import com.smp.extmapper.internals.providers.AnnotationProviderImpl
import com.smp.extmapper.internals.providers.provide
import com.squareup.kotlinpoet.ksp.toClassName
import java.util.Locale

internal interface FileNameFactory {
    fun create(annotatedClass: KSClassDeclaration, mapDirection: MapDirection): String
}

internal class FileNameFactoryImpl : FileNameFactory {
    private val annotationProvider: AnnotationProvider = AnnotationProviderImpl()

    override fun create(annotatedClass: KSClassDeclaration, mapDirection: MapDirection): String =
        StringBuilder()
            .append(annotatedClass.simpleName.asString())
            .append(
                mapDirection.name
                    .lowercase()
                    .replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
            )
            .append(getOtherClassName(annotatedClass, mapDirection))
            .toString()

    private fun getOtherClassName(annotatedClass: KSClassDeclaration, mapDirection: MapDirection) =
        when (mapDirection) {
            MapDirection.FROM -> annotationProvider.provide<ExtMapFrom>(annotatedClass)
                .arguments
                .first { it.name?.asString() == ExtMapFrom::fromClass.name }
                .run { value as KSType }
                .toClassName()
                .simpleName

            MapDirection.TO -> annotationProvider.provide<ExtMapTo>(annotatedClass)
                .arguments
                .first { it.name?.asString() == ExtMapFrom::fromClass.name }
                .value
                .run { this as KSType }
                .toClassName()
                .simpleName
        }
}