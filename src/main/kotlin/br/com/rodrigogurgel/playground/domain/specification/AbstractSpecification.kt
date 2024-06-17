package br.com.rodrigogurgel.playground.domain.specification

sealed class AbstractSpecification<T> : Specification<T> {
    override fun and(specification: Specification<T>): Specification<T> = AndSpecification(this, specification)
}
