package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.processed

import java.time.OffsetDateTime
import java.util.UUID

data class TransactionProcessed(
    val transactionId: UUID,
    val correlationId: UUID,
    val status: TransactionStatusProcessed,
    val message: String?,
    val createdBy: String,
    val createdFrom: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
