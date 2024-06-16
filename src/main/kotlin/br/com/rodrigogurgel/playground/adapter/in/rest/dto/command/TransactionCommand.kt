package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command

import java.util.UUID

data class TransactionCommand(
    val correlationId: UUID,
    val createdBy: String,
)
