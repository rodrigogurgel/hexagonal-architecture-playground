package br.com.rodrigogurgel.playground.application.exception.event

data class ProduceException(override val message: String? = null, override val cause: Throwable?) :
    RuntimeException(message, cause)
