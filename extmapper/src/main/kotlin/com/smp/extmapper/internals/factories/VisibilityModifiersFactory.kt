package com.smp.extmapper.internals.factories

import com.smp.extmapper.internals.VisibilityModifier
import com.squareup.kotlinpoet.KModifier

internal interface VisibilityModifiersFactory {
    fun create(visibilityModifier: VisibilityModifier): KModifier
}

internal class VisibilityModifiersFactoryImpl : VisibilityModifiersFactory {
    override fun create(visibilityModifier: VisibilityModifier): KModifier =
        when (visibilityModifier) {
            VisibilityModifier.PUBLIC -> KModifier.PUBLIC
            VisibilityModifier.INTERNAL -> KModifier.INTERNAL
        }
}