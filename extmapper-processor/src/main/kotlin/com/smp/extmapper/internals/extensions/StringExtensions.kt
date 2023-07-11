package com.smp.extmapper.internals.extensions

import com.squareup.kotlinpoet.CodeBlock

internal fun String.toCodeBlock(vararg args: Any?) =
    CodeBlock.builder()
        .add(this, *args)
        .build()