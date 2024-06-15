package br.com.rodrigogurgel.playground.domain

import java.time.Instant

data class Mail(
    val body: String,
    val from: String,
    val subject: String?,
    val to: String,
    val type: MailType,
    var sentAt: Instant? = null,
) {
    fun setAsSent() {
        this.sentAt = Instant.now()
    }
}
