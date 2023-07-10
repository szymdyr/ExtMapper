package com.smp.extmapper.annotations

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ExtMapFrom(
    val fromClass: KClass<*>,
    val visibilityModifier: Int = PUBLIC
) {
    companion object {
        const val PUBLIC = 0
        const val INTERNAL = 1
    }
}