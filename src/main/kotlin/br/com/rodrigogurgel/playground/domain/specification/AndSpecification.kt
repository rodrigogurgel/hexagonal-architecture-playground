package br.com.rodrigogurgel.playground.domain.specification

class AndSpecification<T>(
    private val spec1: Specification<T>,
    private val spec2: Specification<T>,
) : AbstractSpecification<T>() {

    override fun isSatisfiedBy(value: T): Boolean {
        return spec1.isSatisfiedBy(value) &&
            spec2.isSatisfiedBy(value)
    }
}
