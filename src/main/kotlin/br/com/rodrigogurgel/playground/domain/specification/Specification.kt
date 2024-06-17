package br.com.rodrigogurgel.playground.domain.specification

sealed interface Specification<T> {
    fun isSatisfiedBy(value: T): Boolean
    fun and(specification: Specification<T>): Specification<T>
}
