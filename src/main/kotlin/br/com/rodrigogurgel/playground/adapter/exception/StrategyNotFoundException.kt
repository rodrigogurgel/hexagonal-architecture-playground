package br.com.rodrigogurgel.playground.adapter.exception

data class StrategyNotFoundException(
    override val message: String? = "No strategy found",
) : RuntimeException(message)
