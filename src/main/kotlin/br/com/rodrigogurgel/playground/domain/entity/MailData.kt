package br.com.rodrigogurgel.playground.domain.entity

data class MailData(
    val body: String,
    val from: String,
    val subject: String?,
    val to: String,
)
