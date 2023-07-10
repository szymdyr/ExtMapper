package com.smp.extmapper.processors

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.smp.extmapper.annotations.ExtMapFrom
import com.smp.extmapper.internals.usecases.ProcessMapFromUseCase
import com.smp.extmapper.internals.usecases.ProcessMapFromUseCaseImpl

class ExtMapFromProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val processMapFromUseCase: ProcessMapFromUseCase = ProcessMapFromUseCaseImpl()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedClasses = resolver.getSymbolsWithAnnotation(ExtMapFrom::class.java.name)
            .filterIsInstance<KSClassDeclaration>()

        if (!annotatedClasses.iterator().hasNext()) return emptyList()

        annotatedClasses.forEach {
            processMapFromUseCase(
                ProcessMapFromUseCase.Config(
                    annotatedClass = it,
                    codeGenerator = environment.codeGenerator
                )
            )
        }

        return emptyList()
    }
}