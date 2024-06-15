package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.processed

data class MailProcessed(
    val transaction: TransactionProcessed,
    val data: MailDataProcessed
)
