package br.com.rodrigogurgel.playground.adapter.exception

data class DefaultStrategyNotFoundException(
    override val message: String? = "No default strategies found! please consider creating a default strategy",
) : RuntimeException(message)
