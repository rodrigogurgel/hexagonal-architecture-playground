package br.com.rodrigogurgel.playground.domain.specification

data object SmsBodySpecification : AbstractSpecification<String>() {
    override fun isSatisfiedBy(value: String): Boolean = value.length <= NUMBER_1000
}
