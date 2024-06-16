package br.com.rodrigogurgel.playground.domain.entity

import java.time.Instant
import java.util.UUID

data class Mail(
    val id: UUID,
    val transaction: Transaction,
    val data: MailData,
    val type: MailType,
    var sentAt: Instant? = null,
) {
    fun toSuccess() {
        this.transaction.updatedAt = Instant.now()
        this.transaction.status = TransactionStatus.SUCCESS
    }

    fun toFailure(message: String) {
        this.transaction.updatedAt = Instant.now()
        this.transaction.status = TransactionStatus.FAILURE
        this.transaction.message = message
    }

    fun setAsSent() {
        sentAt = Instant.now()
    }
}
