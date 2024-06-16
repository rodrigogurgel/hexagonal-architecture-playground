package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response

import java.time.OffsetDateTime
import java.util.UUID

data class MailResponse(
    val id: UUID,
    val transaction: TransactionResponse,
    val data: MailDataResponse,
    val type: MailTypeResponse,
    val sentAt: OffsetDateTime?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
