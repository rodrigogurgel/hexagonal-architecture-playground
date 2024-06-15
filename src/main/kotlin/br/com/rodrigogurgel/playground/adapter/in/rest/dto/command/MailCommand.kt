package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command

sealed class MailCommand(
    open val transaction: TransactionCommand,
    open val data: MailDataCommand
)
