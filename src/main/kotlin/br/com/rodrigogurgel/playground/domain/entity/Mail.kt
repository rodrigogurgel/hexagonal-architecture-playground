package br.com.rodrigogurgel.playground.domain.entity

import br.com.rodrigogurgel.playground.domain.policy.MailValidator
import java.time.Instant
import java.util.UUID

data class Mail(
    val id: UUID,
    var transaction: Transaction,
    val data: MailData,
    val type: MailType,
) {
    var updatedAt: Instant = Instant.now()
        private set

    init {
        updatedAt = Instant.now()
    }

    var sentAt: Instant? = null
        private set

    val createdAt: Instant = Instant.now()

    fun toSentWithSuccess() {
        sentAt = Instant.now()
        updatedAt = Instant.now()
        this.transaction = transaction.toSuccess()
    }

    fun toSentWithFailure(message: String) {
        updatedAt = Instant.now()
        this.transaction = transaction.toFailure(message)
    }

    fun validate(policy: MailValidator) = policy.validate(this)
}
