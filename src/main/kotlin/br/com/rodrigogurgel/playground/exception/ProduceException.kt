package br.com.rodrigogurgel.playground.exception

data class ProduceException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)
