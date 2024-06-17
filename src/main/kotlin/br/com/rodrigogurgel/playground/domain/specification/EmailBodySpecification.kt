package br.com.rodrigogurgel.playground.domain.specification

data object EmailBodySpecification : AbstractSpecification<String>() {
    override fun isSatisfiedBy(value: String): Boolean = value.length <= NUMBER_1000
}
