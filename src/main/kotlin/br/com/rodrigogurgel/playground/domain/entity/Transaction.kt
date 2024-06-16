package br.com.rodrigogurgel.playground.domain.entity

import java.time.Instant
import java.util.UUID

data class Transaction(
    val correlationId: UUID,
    val status: TransactionStatus = TransactionStatus.PROCESSING,
    val message: String? = null,
    val createdBy: String,
    val createdFrom: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    fun toSuccess(): Transaction {
        return copy(
            updatedAt = Instant.now(),
            status = TransactionStatus.SUCCESS
        )
    }

    fun toFailure(message: String): Transaction {
        return copy(
            updatedAt = Instant.now(),
            status = TransactionStatus.FAILURE,
            message = message
        )
    }
}
