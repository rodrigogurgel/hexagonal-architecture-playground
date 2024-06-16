package br.com.rodrigogurgel.playground.adapter.mapper.event

import br.com.rodrigogurgel.playground.adapter.`in`.extension.toUUID
import br.com.rodrigogurgel.playground.domain.entities.Mail
import br.com.rodrigogurgel.playground.domain.entities.MailData
import br.com.rodrigogurgel.playground.domain.entities.MailType
import br.com.rodrigogurgel.playground.domain.entities.Transaction
import br.com.rodrigogurgel.playground.domain.entities.TransactionStatus
import br.com.rodrigogurgel.playground.exception.MapperException
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.MailTypeCommand
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import java.time.Instant
import java.util.UUID

fun MailCommand.toDomain(): Result<Mail, Throwable> = runCatching {
    Mail(
        id = UUID.randomUUID(),
        data = data.run {
            MailData(
                body = body.toString(),
                from = from.toString(),
                subject = subject.toString(),
                to = to.toString(),
            )
        },
        transaction = transaction.run {
            Transaction(
                correlationId = correlationId.toUUID(),
                status = TransactionStatus.PROCESSING,
                createdBy = createdBy.toString(),
                createdFrom = createdFrom.toString(),
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            )
        },
        type = type.toDomain()
    )
}.mapError { throwable ->
    MapperException(
        "An error occurred while try convert MailCommand to Mai",
        throwable
    )
}

fun MailTypeCommand.toDomain(): MailType = when (this) {
    MailTypeCommand.SMS -> MailType.SMS
    MailTypeCommand.EMAIL -> MailType.EMAIL
    MailTypeCommand.WHATSAPP -> MailType.WHATSAPP
}
