package br.com.rodrigogurgel.playground.domain.entity

import java.time.Instant
import java.util.UUID

data class Mail(
    val id: UUID,
    var transaction: Transaction,
    val data: MailData,
    val type: MailType,
    var sentAt: Instant? = null,
) {
    fun toSuccess() {
        this.transaction = transaction.toSuccess()
    }

    fun toFailure(message: String) {
        this.transaction = transaction.toFailure(message)
    }

    fun setAsSent() {
        sentAt = Instant.now()
    }
}
