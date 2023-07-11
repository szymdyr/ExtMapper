package com.smp.extmapper.annotations

import com.smp.extmapper.providers.EmptyFallbackValueProvider
import kotlin.reflect.KClass

/**
 * An annotations that provides fallback value strategy if mapping from nullable value to non
 * nullable one.
 *
 * @property forceUnwrap - if set to true then !! operator will be used
 * @property fallbackValueProvider - if [forceUnwrap] is set to false then [fallbackValueProvider]
 * is used. [EmptyFallbackValueProvider] can be used only of [forceUnwrap] is set to true!
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY)
annotation class ExtMapFallbackValue(
    val forceUnwrap: Boolean = true,
    val fallbackValueProvider: KClass<*> = EmptyFallbackValueProvider::class
)
