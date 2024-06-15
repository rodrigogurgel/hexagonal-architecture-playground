package br.com.rodrigogurgel.playground.application.exception.common

data class MapperException(override val message: String?, override val cause: Throwable) : Exception(message, cause)
