package br.com.rodrigogurgel.playground.exception

data class TransactionAlreadyExistsException(
    override val message: String? = "Transaction already exists",
) : RuntimeException(message)
