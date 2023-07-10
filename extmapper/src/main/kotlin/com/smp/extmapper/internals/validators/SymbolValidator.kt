package com.smp.extmapper.internals.validators

import com.google.devtools.ksp.symbol.KSAnnotated

internal fun interface SymbolValidator {
    fun isValid(symbol: KSAnnotated): Boolean
}