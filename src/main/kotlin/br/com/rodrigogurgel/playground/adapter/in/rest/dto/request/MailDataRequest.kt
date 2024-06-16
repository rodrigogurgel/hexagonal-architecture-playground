package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.request

data class MailDataRequest(
    val body: String,
    val from: String,
    val subject: String?,
    val to: String,
)
