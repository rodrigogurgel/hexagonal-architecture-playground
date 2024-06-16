package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response

import java.time.OffsetDateTime
import java.util.UUID

data class TransactionResponse(
    val correlationId: UUID,
    val status: TransactionStatusResponse,
    val message: String?,
    val createdBy: String,
    val createdFrom: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
