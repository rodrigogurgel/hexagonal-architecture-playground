package br.com.rodrigogurgel.playground.adapter.exception

data class ProducerException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)
