package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command

data class SmsCommand(
    override val transaction: TransactionCommand,
    override val data: MailDataCommand
) : MailCommand(transaction, data)
