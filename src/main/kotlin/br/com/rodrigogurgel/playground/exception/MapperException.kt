package br.com.rodrigogurgel.playground.exception

data class MapperException(
    override val message: String?,
    override val cause: Throwable,
) : RuntimeException(message, cause)
