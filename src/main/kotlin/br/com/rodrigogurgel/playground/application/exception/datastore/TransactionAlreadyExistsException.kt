package br.com.rodrigogurgel.playground.application.exception.datastore

import java.util.UUID

data class TransactionAlreadyExistsException(val transactionId: UUID) : RuntimeException(
    "Transaction $transactionId already exists"
)
