package com.smp.extmapper.annotations

/**
 * An annotation that indicates that the given property should be mapped to field with provided
 * name (or names)
 * @property name
 * @property alternativeNames
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY)
annotation class ExtMapPropertyName(
    val name: String,
    val alternativeNames: Array<String> = []
)