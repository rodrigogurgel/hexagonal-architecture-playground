package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command

data class EmailCommand(
    override val transaction: TransactionCommand,
    override val data: MailDataCommand
) : MailCommand(transaction, data)
