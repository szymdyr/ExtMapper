package com.smp.extmapper.processors

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.smp.extmapper.annotations.ExtMapFrom
import com.smp.extmapper.internals.validators.ExtMapFromSymbolAnnotatedValidatorImpl
import com.smp.extmapper.internals.validators.SymbolValidator
import com.smp.extmapper.internals.visitors.ExtMapFromVisitor

class ExtMapFromProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val validator: SymbolValidator = ExtMapFromSymbolAnnotatedValidatorImpl()
    private val visitor: KSVisitorVoid =
        ExtMapFromVisitor(environment.codeGenerator)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotationName = ExtMapFrom::class.qualifiedName ?: return emptyList()
        environment.logger
        val annotatedClasses = resolver.getSymbolsWithAnnotation(annotationName)
            .filterIsInstance<KSClassDeclaration>()
            .toList()

        annotatedClasses
            .filter { validator.isValid(it) }
            .onEach { it.accept(visitor, Unit) }

        return annotatedClasses.filterNot { it.validate() }
    }
}