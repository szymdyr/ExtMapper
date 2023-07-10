package com.smp.extmapper.internals.usecases

internal interface UseCase<T> {
    operator fun invoke(param: T)
}