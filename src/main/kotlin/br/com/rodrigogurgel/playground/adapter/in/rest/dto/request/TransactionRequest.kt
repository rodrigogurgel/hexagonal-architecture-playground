package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.request

import java.util.UUID

data class TransactionRequest(
    val correlationId: UUID,
    val createdBy: String,
)
