package br.com.rodrigogurgel.playground.domain.specification

data object BrazilianPhoneNumberSpecification : AbstractSpecification<String>() {
    private const val BRAZILIAN_PHONE_NUMBER_REGEX =
        """^(?:(?:\+|00)?(55)\s?)?(?:\(?([1-9][0-9])\)?\s?)?(((?:9\d|[2-9])\d{3})?(\d{4}))"""

    override fun isSatisfiedBy(value: String): Boolean = value.matches(BRAZILIAN_PHONE_NUMBER_REGEX.toRegex())
}
