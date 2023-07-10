package com.smp.extmapper.internals.factories

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.smp.extmapper.internals.MapDirection
import com.smp.extmapper.internals.providers.ExtMapFromArgumentsValueProvider
import com.smp.extmapper.internals.providers.ExtMapFromArgumentsValueProviderImpl
import com.smp.extmapper.internals.providers.ExtMapToArgumentsValueProvider
import com.smp.extmapper.internals.providers.ExtMapToArgumentsValueProviderImpl
import com.squareup.kotlinpoet.ksp.toClassName
import java.util.Locale

internal interface FileNameFactory {
    fun create(annotatedClass: KSClassDeclaration, mapDirection: MapDirection): String
}

internal class FileNameFactoryImpl : FileNameFactory {
    private val extMapFromArgumentsValueProvider: ExtMapFromArgumentsValueProvider =
        ExtMapFromArgumentsValueProviderImpl()

    private val extMapToArgumentsValueProvider: ExtMapToArgumentsValueProvider =
        ExtMapToArgumentsValueProviderImpl()

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
            MapDirection.FROM -> extMapFromArgumentsValueProvider.getFromClassValue(annotatedClass)
                .toClassName()
                .simpleName

            MapDirection.TO -> extMapToArgumentsValueProvider.getToClassValue(annotatedClass)
                .toClassName()
                .simpleName
        }
}