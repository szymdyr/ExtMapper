package com.smp.extmapper.internals.usecases

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.smp.extmapper.annotations.ExtMapFrom
import com.smp.extmapper.internals.MapDirection
import com.smp.extmapper.internals.factories.FileFactory
import com.smp.extmapper.internals.factories.FileFactoryImpl
import com.smp.extmapper.internals.factories.FileNameFactory
import com.smp.extmapper.internals.factories.FileNameFactoryImpl
import com.smp.extmapper.internals.providers.AnnotationProvider
import com.smp.extmapper.internals.providers.AnnotationProviderImpl
import com.smp.extmapper.internals.providers.provide
import com.smp.extmapper.internals.writers.FileWriter
import com.smp.extmapper.internals.writers.FileWriterImpl
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName

internal interface ProcessMapFromUseCase : UseCase<ProcessMapFromUseCase.Config> {
    data class Config(
        val annotatedClass: KSClassDeclaration,
        val codeGenerator: CodeGenerator
    )
}

internal class ProcessMapFromUseCaseImpl : ProcessMapFromUseCase {
    private val fileFactory: FileFactory = FileFactoryImpl()
    private val fileWriter: FileWriter = FileWriterImpl()
    private val fileNameFactory: FileNameFactory = FileNameFactoryImpl()
    private val annotationProvider: AnnotationProvider = AnnotationProviderImpl()

    override operator fun invoke(param: ProcessMapFromUseCase.Config) {
        val fileName = fileNameFactory.create(param.annotatedClass, MapDirection.FROM)
        val funSpec = createFunSpec(param.annotatedClass)

        val fileSpec = createFileSpec(param.annotatedClass, funSpec, fileName)
        val outputFile = fileFactory.create(param.codeGenerator, fileSpec, param.annotatedClass)
        fileWriter.write(outputFile, fileSpec)
    }

    private fun createFunSpec(
        annotatedClass: KSClassDeclaration
    ): FunSpec =
        FunSpec.builder(
            StringBuilder()
                .append("to")
                .append(annotatedClass.simpleName.asString())
                .toString()
        )
            .receiver(
                annotationProvider.provide<ExtMapFrom>(annotatedClass)
                    .arguments
                    .first { it.name?.asString() == ExtMapFrom::fromClass.name }
                    .run { value as KSType }
                    .toClassName()
            )
            .returns(annotatedClass.toClassName())
            .addStatement("return «${annotatedClass.toClassName().simpleName}(")
            .addAllFields(annotatedClass)
            .addStatement(")")
            .build()

    private fun FunSpec.Builder.addAllFields(annotatedClass: KSClassDeclaration): FunSpec.Builder =
        apply {
            annotatedClass.getAllProperties()
                .forEach {
                    addStatement("\t${it.simpleName.asString()} = ${it.simpleName.asString()},")
                }
        }

    private fun createFileSpec(
        annotatedClass: KSClassDeclaration,
        funSpec: FunSpec,
        fileName: String
    ) = FileSpec.builder(
        packageName = annotatedClass.packageName.asString(),
        fileName = fileName
    )
        .addFunction(funSpec)
        .build()
}