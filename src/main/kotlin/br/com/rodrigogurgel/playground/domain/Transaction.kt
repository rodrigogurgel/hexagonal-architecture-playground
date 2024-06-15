package br.com.rodrigogurgel.playground.domain

import java.time.Instant
import java.util.UUID

data class Transaction<T>(
    val transactionId: UUID,
    val correlationId: UUID,
    var status: TransactionStatus = TransactionStatus.PROCESSING,
    var message: String? = null,
    val data: T,
    val createdBy: String,
    val createdFrom: String,
    val createdAt: Instant,
    var updatedAt: Instant,
) {
    fun toSuccess() {
        this.updatedAt = Instant.now()
        this.status = TransactionStatus.SUCCESS
    }

    fun toFailure(message: String) {
        this.updatedAt = Instant.now()
        this.status = TransactionStatus.FAILURE
        this.message = message
    }
}
