package br.com.rodrigogurgel.playground.domain.specification

data object EmailAddressSpecification : AbstractSpecification<String>() {
    private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"

    override fun isSatisfiedBy(value: String): Boolean = value.matches(EMAIL_REGEX.toRegex())
}
