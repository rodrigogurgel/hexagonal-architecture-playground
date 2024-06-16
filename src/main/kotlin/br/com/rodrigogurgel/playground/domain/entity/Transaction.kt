package br.com.rodrigogurgel.playground.domain.entity

import java.util.UUID

data class Transaction(
    val correlationId: UUID,
    val status: TransactionStatus = TransactionStatus.PROCESSING,
    val message: String? = null,
    val createdBy: String,
    val createdFrom: String,
) {
    fun toSuccess(): Transaction {
        return copy(
            status = TransactionStatus.SUCCESS
        )
    }

    fun toFailure(message: String): Transaction {
        return copy(
            status = TransactionStatus.FAILURE,
            message = message
        )
    }
}
