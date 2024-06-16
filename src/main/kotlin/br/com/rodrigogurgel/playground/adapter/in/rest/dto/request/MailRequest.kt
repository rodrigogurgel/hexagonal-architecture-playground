package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.request

import java.util.UUID

open class MailRequest(
    val id: UUID,
    open val transaction: TransactionRequest,
    open val data: MailDataRequest,
)

data class AsyncMailRequest(
    override val transaction: TransactionRequest,
    override val data: MailDataRequest,
    val type: MailTypeRequest,
) : MailRequest(UUID.randomUUID(), transaction, data)
