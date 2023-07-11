package com.smp.extmapper.providers

interface FallbackValueProvider<TInput, TOutput> {
    fun provide(input: TInput): TOutput
}

class EmptyFallbackValueProvider: FallbackValueProvider<Unit, Unit> {
    override fun provide(input: Unit) = Unit
}