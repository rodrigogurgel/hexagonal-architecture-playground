package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command

data class AsyncMailCommand(
    override val transaction: TransactionCommand,
    override val data: MailDataCommand,
    val type: MailTypeCommand,
) : MailCommand(transaction, data)
