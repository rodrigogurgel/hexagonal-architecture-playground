package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.processed

data class MailDataProcessed(
    val body: String,
    val from: String,
    val subject: String?,
    val to: String,
    val type: MailTypeProcessed,
)
