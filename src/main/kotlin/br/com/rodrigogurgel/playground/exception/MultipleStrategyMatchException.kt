package br.com.rodrigogurgel.playground.exception

data class MultipleStrategyMatchException(override val message: String? = "Multiple strategy match exception") :
    RuntimeException(message)
