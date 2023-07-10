package com.smp.extmapper.internals.extensions

import com.google.devtools.ksp.symbol.KSAnnotation

internal fun KSAnnotation.getArgument(argumentName: String) =
    arguments.first { it.name?.asString() == argumentName }