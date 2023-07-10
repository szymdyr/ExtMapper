package com.smp.extmapper.internals.factories

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import java.io.OutputStream

internal interface FileFactory {
    fun create(
        codeGenerator: CodeGenerator,
        fileSpec: FileSpec,
        annotatedClass: KSClassDeclaration
    ): OutputStream
}

internal class FileFactoryImpl : FileFactory {
    override fun create(
        codeGenerator: CodeGenerator,
        fileSpec: FileSpec,
        annotatedClass: KSClassDeclaration
    ): OutputStream =
        codeGenerator.createNewFile(
            Dependencies(
                aggregating = false,
                sources = annotatedClass.containingFile?.let { arrayOf(it) } ?: arrayOf(),
            ),
            fileName = fileSpec.name,
            packageName = fileSpec.packageName
        )
}