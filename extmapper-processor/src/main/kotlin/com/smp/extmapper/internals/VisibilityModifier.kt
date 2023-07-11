package com.smp.extmapper.internals

internal enum class VisibilityModifier {
    PUBLIC,
    INTERNAL
}

internal fun Int.toVisibilityModifier(): VisibilityModifier=
    VisibilityModifier.values()[this]