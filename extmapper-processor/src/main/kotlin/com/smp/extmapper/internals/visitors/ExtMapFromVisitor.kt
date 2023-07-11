package com.smp.extmapper.internals.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.smp.extmapper.internals.MapDirection
import com.smp.extmapper.internals.factories.FileFactory
import com.smp.extmapper.internals.factories.FileFactoryImpl
import com.smp.extmapper.internals.factories.FileNameFactory
import com.smp.extmapper.internals.factories.FileNameFactoryImpl
import com.smp.extmapper.internals.factories.PropertyNameFactory
import com.smp.extmapper.internals.factories.PropertyNameFactoryImpl
import com.smp.extmapper.internals.factories.VisibilityModifiersFactory
import com.smp.extmapper.internals.factories.VisibilityModifiersFactoryImpl
import com.smp.extmapper.internals.providers.ExtMapFromArgumentsValueProvider
import com.smp.extmapper.internals.providers.ExtMapFromArgumentsValueProviderImpl
import com.smp.extmapper.internals.toVisibilityModifier
import com.smp.extmapper.internals.writers.FileWriter
import com.smp.extmapper.internals.writers.FileWriterImpl
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName

internal class ExtMapFromVisitor(
    private val codeGenerator: CodeGenerator
) : KSVisitorVoid() {

    private val fileFactory: FileFactory = FileFactoryImpl()
    private val fileWriter: FileWriter = FileWriterImpl()
    private val fileNameFactory: FileNameFactory = FileNameFactoryImpl()
    private val argumentsValueProvider: ExtMapFromArgumentsValueProvider =
        ExtMapFromArgumentsValueProviderImpl()

    private val visibilityModifiersFactory: VisibilityModifiersFactory =
        VisibilityModifiersFactoryImpl()

    private val propertyNameFactory: PropertyNameFactory = PropertyNameFactoryImpl()

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val fileName = fileNameFactory.create(classDeclaration, MapDirection.FROM)
        val fromClass = argumentsValueProvider.getFromClassValue(classDeclaration)
        val funSpec = createFunSpec(classDeclaration, fromClass)

        val fileSpec = createFileSpec(classDeclaration, funSpec, fileName)
        val outputFile = fileFactory.create(codeGenerator, fileSpec, classDeclaration)
        fileWriter.write(outputFile, fileSpec)
    }

    private fun createFunSpec(
        annotatedClass: KSClassDeclaration,
        fromClass: KSClassDeclaration
    ): FunSpec =
        FunSpec.builder(
            StringBuilder()
                .append("to")
                .append(annotatedClass.simpleName.asString())
                .toString()
        )
            .addVisibilityModifier(annotatedClass)
            .receiver(fromClass.toClassName())
            .returns(annotatedClass.toClassName())
            .addStatement("return «${annotatedClass.toClassName().simpleName}(")
            .addAllFields(annotatedClass, fromClass)
            .addStatement(")")
            .build()

    private fun FunSpec.Builder.addAllFields(
        annotatedClass: KSClassDeclaration,
        fromClass: KSClassDeclaration
    ): FunSpec.Builder =
        apply {
            annotatedClass.getAllProperties()
                .forEach {
                    addStatement("\t${it.simpleName.asString()} = ${propertyNameFactory.create(it, fromClass)},")
                }
        }

    private fun FunSpec.Builder.addVisibilityModifier(annotatedClass: KSClassDeclaration): FunSpec.Builder =
        apply {
            val visibilityModifier =
                argumentsValueProvider.getVisibilityModifierValue(annotatedClass)
                    .toVisibilityModifier()

            addModifiers(
                visibilityModifiersFactory.create(visibilityModifier)
            )
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