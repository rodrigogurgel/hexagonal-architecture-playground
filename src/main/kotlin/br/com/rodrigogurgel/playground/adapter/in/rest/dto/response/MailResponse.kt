package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response

import java.time.OffsetDateTime
import java.util.UUID

data class MailResponse(
    val id: UUID,
    val transaction: TransactionResponse,
    val data: MailDataResponse,
    val type: MailTypeResponse,
    var sentAt: OffsetDateTime? = null,
)
