package com.smp.extmapper.annotations

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ExtMapTo(
    val toClass: KClass<*>
)