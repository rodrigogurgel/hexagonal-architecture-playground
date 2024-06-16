package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command

import java.util.UUID

sealed class MailCommand(
    open val transaction: TransactionCommand,
    open val data: MailDataCommand,
) {
    data class AsyncMailCommand(
        override val transaction: TransactionCommand,
        override val data: MailDataCommand,
        val type: MailTypeCommand,
    ) : MailCommand(transaction, data)

    data class EmailCommand(
        val id: UUID,
        override val transaction: TransactionCommand,
        override val data: MailDataCommand,
    ) : MailCommand(transaction, data)

    data class SmsCommand(
        val id: UUID,
        override val transaction: TransactionCommand,
        override val data: MailDataCommand,
    ) : MailCommand(transaction, data)

    data class WhatsAppCommand(
        val id: UUID,
        override val transaction: TransactionCommand,
        override val data: MailDataCommand,
    ) : MailCommand(transaction, data)
}
