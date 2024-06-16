package br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response

data class MailDataResponse(
    val body: String,
    val from: String,
    val subject: String?,
    val to: String,
)
