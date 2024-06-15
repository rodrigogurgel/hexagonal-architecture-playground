package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command

data class MailDataCommand(
    val body: String,
    val from: String,
    val subject: String?,
    val to: String,
)
