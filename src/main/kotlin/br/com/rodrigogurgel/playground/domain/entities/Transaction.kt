package br.com.rodrigogurgel.playground.domain.entities

import java.time.Instant
import java.util.UUID

data class Transaction(
    val correlationId: UUID,
    var status: TransactionStatus = TransactionStatus.PROCESSING,
    var message: String? = null,
    val createdBy: String,
    val createdFrom: String,
    val createdAt: Instant,
    var updatedAt: Instant,
)
