package br.com.rodrigogurgel.playground.adapter.exception

data class MultipleStrategyMatchException(
    override val message: String? = "Multiple strategy match exception",
) : RuntimeException(message)
